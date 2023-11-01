package com.aristo.admin.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aristo.admin.Datas.AdminDataHolder
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.databinding.ActivityHomeBinding
import com.aristo.admin.model.Admin
import com.aristo.admin.view.Categories.AddProducts.CreateMainCategoryActivity
import com.aristo.admin.view.Notification.SendNotificationActivity
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private var isLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCenter.start(
            application, "9e748b4f-85ad-454e-a939-60a8889e7808\"",
            Analytics::class.java, Crashes::class.java
        )

        // Edit Admin Information
        binding.progressBar.visibility = View.VISIBLE

        checkUserSignin()

        binding.btnEdit.setOnClickListener {
            if (isLoading){
                Toast.makeText(this, "Loading, Please wait...", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(applicationContext, EditActivity::class.java))
            }
        }
        binding.btnAddNewProduct.setOnClickListener {
            startActivity(Intent(applicationContext, CreateMainCategoryActivity::class.java))
        }
        binding.btnRecentProducts.setOnClickListener {
            startActivity(Intent(applicationContext, MainCategoriesActivity::class.java))
        }
        binding.btnHelpCenter.setOnClickListener {
            startActivity(Intent(applicationContext, HelpCenterActivity::class.java))
        }

        binding.btnNotification.setOnClickListener {
            startActivity(Intent(applicationContext, SendNotificationActivity::class.java))
        }
    }

    fun checkUserSignin(){

        var auth: FirebaseAuth = Firebase.auth

        if (auth.currentUser == null){

            var email = "tunlinaung.tla7@gmail.com"
            var password = "Superst@r7"

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->

                if (task.isSuccessful){

                    fetchAdminData()

                }
                else{
                    Toast.makeText(this,"${task.exception!!.localizedMessage.toString()}",Toast.LENGTH_LONG).show()

                }

            }
        }
        else{
            fetchAdminData()
        }
    }

    fun fetchAdminData(){
        CategoryFirebase.getAdmin { isSuccess, result ->

            if (isSuccess) {
                if (result != null) {
                    result as Admin
                    AdminDataHolder.instance.admin = result
                }
            } else {
//                Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_LONG).show()
            }
            isLoading = false
            binding.progressBar.visibility = View.GONE
        }
    }
}