package com.aristo.admin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.Manager.Network.fetchSpecificUserDeviceTokens
import com.aristo.admin.R
import com.aristo.admin.databinding.ActivityAddPointBinding
import com.aristo.admin.databinding.ActivityUserIdListBinding
import com.aristo.admin.model.User
import com.aristo.admin.view.Notification.NotificationData
import com.aristo.admin.view.Notification.PushNotification
import com.aristo.admin.view.Notification.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic2"
class AddPointActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddPointBinding
    private lateinit var user : User
    var point : Int? = null
    var deviceToken : String = ""
    private var notificationsSentCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPointBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        user = intent.getSerializableExtra("user") as User

        point = user.point

        binding.tvId.text = "${user.userId}"
        binding.tvCurrentPoint.text = "${user.point}"

        fetchSpecificUserDeviceTokens(user.userId.toString()) { success, data ->
            if (success) {
                deviceToken = data
            }
        }

        binding.btnAddPoint.setOnClickListener {

            binding.addProgress.visibility = View.VISIBLE

            if (binding.etPoint.text.isNotEmpty()){
                point = binding.etPoint.text.toString().toInt() + user.point

                CategoryFirebase.addPoints(point!!, user.phone){ success, message ->

                    if (success) {

                        binding.tvCurrentPoint.text = "$point"
                        sendNoti()
                        Toast.makeText(this, "Points Added Successfully.", Toast.LENGTH_LONG).show()
                        finish()

                    }
                    else{
                        Toast.makeText(this, "Points Added Failed.", Toast.LENGTH_LONG).show()
                        binding.addProgress.visibility = View.GONE
                    }
                }
            }
            else{
                Toast.makeText(this, "Enter points to add", Toast.LENGTH_LONG).show()

            }

        }

    }

    fun sendNoti(){
        val title = "Point Added"
        val message = "သင့် account သို့ " + binding.etPoint.text.toString() + " points ထည့်သွင်းလိုက်ပါပြီ"
        notificationsSentCount = 0

        if (message.isNotEmpty()) {

            PushNotification(
                NotificationData(title, "", message),
                deviceToken
            ).also {

                sendNotification(it)
            }
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
//                runOnUiThread {
//                    // Show an error message to the user
//                    binding.progressBar.visibility = View.GONE
//                    showErrorToast("Failed to send notification")
//                }
            }
        } catch(e: Exception) {
            // Handle the exception, for example, network issues
            Log.e("Response:", "Exception: ${e.toString()}")

            // Perform actions accordingly, e.g., show an error message to the user
//            runOnUiThread {
//                // Show an error message to the user
//                binding.progressBar.visibility = View.GONE
//                showErrorToast("Failed to send notification")
//            }
        }
    }
}