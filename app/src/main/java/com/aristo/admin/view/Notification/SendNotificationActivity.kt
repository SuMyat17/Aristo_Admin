package com.aristo.admin.view.Notification

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.aristo.admin.Manager.Network.CategoryFirebase
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

const val TOPIC = "/topics/myTopic2"
class SendNotificationActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySendNotificationBinding
    var userTokenLists : ArrayList<String> = arrayListOf()
    private var notificationsSentCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySendNotificationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        fetchUserDeviceTokens{ isSuccess, tokens ->

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

            for (token in userTokenLists){

                if(message.isNotEmpty()) {
                    PushNotification(
                        NotificationData(title, message),
                        token
                    ).also {
                        sendNotification(it)
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
                    binding.btnSend.alpha = 0.5f
                }
            }
        }
    }

    // Function to show an error message to the user (you can customize this based on your UI)
    private fun showErrorToast(message: String) {
        Toast.makeText(this@SendNotificationActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun fetchUserDeviceTokens(completionHandler: (Boolean, ArrayList<String>?) -> Unit){

        val databaseReference = FirebaseDatabase.getInstance().getReference("Tokens")
        val tokenList: ArrayList<String> = ArrayList()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // whenever data at this location is updated.
                for (childSnapshot in dataSnapshot.children) {
                    val token = childSnapshot.child("token").getValue(String::class.java)
                    // Handle the token data as needed
                    if (token != null) {
                        tokenList.add(token)
                    }

                }

                Log.d("Token", "Token List $tokenList")
                completionHandler(true,tokenList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Log.w("Firebase", "Failed to read value.", databaseError.toException())
                completionHandler(false,null)
            }
        })

    }

}