package com.aristo.admin.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aristo.admin.databinding.ActivityHomeBinding
import com.aristo.admin.view.Categories.AddProducts.CreateMainCategoryActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCenter.start(
            application, "9e748b4f-85ad-454e-a939-60a8889e7808\"",
            Analytics::class.java, Crashes::class.java
        )

        binding.btnEdit.setOnClickListener {
            startActivity(Intent(applicationContext, EditActivity::class.java))
        }
        binding.btnAddNewProduct.setOnClickListener {
            startActivity(Intent(applicationContext, CreateMainCategoryActivity::class.java))
        }
        binding.btnRecentProducts.setOnClickListener {
            startActivity(Intent(applicationContext, MainCategoriesActivity::class.java))
        }
        binding.btnHelpCenter.setOnClickListener {

        }
    }
}