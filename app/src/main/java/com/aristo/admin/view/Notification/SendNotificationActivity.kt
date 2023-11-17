package com.aristo.admin.view.Notification

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.Manager.Network.fetchUserDeviceTokens
import com.aristo.admin.R
import com.aristo.admin.databinding.ActivitySendNotificationBinding
import com.aristo.admin.model.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Callback
import kotlin.math.log

const val TOPIC = "/topics/myTopic2"
class SendNotificationActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySendNotificationBinding
    var userTokenLists : ArrayList<String> = arrayListOf()
    private var notificationsSentCount = 0
    var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySendNotificationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        fetchUserDeviceTokens { isSuccess, tokens ->

            if (tokens != null) {
                userTokenLists = tokens
            }

            Log.d("\"Response:", "Response Token List $userTokenLists")
        }

        binding.btnSend.setOnClickListener {
            val title = "New Arrival"
            val message = binding.tvNotiMessage.text.toString()
            notificationsSentCount = 0

            binding.progressBar.visibility = View.VISIBLE

            selectedImageUri?.let { it1 ->
                CategoryFirebase.uploadImageToFirebase(it1) { isSuccess, imageUrl ->
                    if (isSuccess) {
                        // Image uploaded successfully, set the imageUrl in the category object
                        if (imageUrl != null) {
                            selectedImageUri = imageUrl.toUri()

                            for (token in userTokenLists){
                                if(message.isNotEmpty()) {

                                    PushNotification(
                                        NotificationData(title,selectedImageUri.toString(), message),
                                        token
                                    ).also {

                                        sendNotification(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (selectedImageUri == null){
                for (token in userTokenLists){
                    if(message.isNotEmpty()) {

                        PushNotification(
                            NotificationData(title,selectedImageUri.toString(), message),
                            token
                        ).also {

                            sendNotification(it)
                        }
                    }
                }
            }

        }

        binding.tvNotiMessage.addTextChangedListener {

            if (binding.tvNotiMessage.text.isEmpty()){
                binding.btnSend.isEnabled = false
                binding.btnSend.alpha = 0.5f
            }
            else{
                binding.btnSend.isEnabled = true
                binding.btnSend.alpha = 1f
            }

        }

        binding.ibBack.setOnClickListener {
            finish()
        }

        // Set selected image url
        val galleryImage = registerForActivityResult(
            ActivityResultContracts. GetContent(),
            ActivityResultCallback { uri ->

                if (uri != null) {
                    selectedImageUri = uri
                    binding.ivProduct.setImageURI(uri)
                }
            })

        // When select image from gallery
        binding.ivProduct.setOnClickListener {
            galleryImage.launch("image/*")
        }

    }

    fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("Response:", "Notification Sent Successfully")
            } else {
                // Handle the case when notification sending fails
                Log.e("Response:", "Failed to send notification: ${response.errorBody().toString()}")

                // Perform actions accordingly, e.g., show an error message to the user
                runOnUiThread {
                    // Show an error message to the user
                    binding.progressBar.visibility = View.GONE
                    showErrorToast("Failed to send notification")
                }
            }
        } catch(e: Exception) {
            // Handle the exception, for example, network issues
            Log.e("Response:", "Exception: ${e.toString()}")

            // Perform actions accordingly, e.g., show an error message to the user
            runOnUiThread {
                // Show an error message to the user
                binding.progressBar.visibility = View.GONE
                showErrorToast("Failed to send notification")
            }
        } finally {
            notificationsSentCount++

            // Check if all notifications are sent
            if (notificationsSentCount == userTokenLists.size) {
                // All notifications are sent, perform your action here
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    showErrorToast("Notification sent successfully.")
                    binding.tvNotiMessage.setText("")
                    binding.btnSend.isEnabled = false
                    selectedImageUri = null
                    binding.ivProduct.setImageURI(null)
                    binding.ivProduct.setImageResource(R.drawable.ic_placeholder)
                    binding.btnSend.alpha = 0.5f
                }
            }
        }
    }

    // Function to show an error message to the user (you can customize this based on your UI)
    private fun showErrorToast(message: String) {
        Toast.makeText(this@SendNotificationActivity, message, Toast.LENGTH_SHORT).show()
    }



}