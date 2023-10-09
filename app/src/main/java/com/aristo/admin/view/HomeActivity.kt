package com.aristo.admin.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aristo.admin.Datas.AdminDataHolder
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.Manager.SharedPreferencesManager.get
import com.aristo.admin.databinding.ActivityHomeBinding
import com.aristo.admin.model.Admin
import com.aristo.admin.view.Categories.AddProducts.CreateMainCategoryActivity
import com.bumptech.glide.Glide
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

        // Edit Admin Information
        val companyName = SharedPreferencesManager.customPrefs(this, "Admin").get("company")

        if (companyName != null) {
            if (AdminDataHolder.instance.admin == null ) {
                binding.progressBar.visibility = View.VISIBLE

                CategoryFirebase.getAdmin(companyName) { isSuccess, result ->
                    if (isSuccess) {
                        if (result != null) {
                            result as Admin
                            binding.tvCompanyName.text = result.companyName
                            if (result.image != null) {
                                Glide.with(this).load(result.image).into(binding.ivCompanyLogo)
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_LONG).show()
                    }
                    binding.progressBar.visibility = View.GONE
                }
            } else {
                binding.tvCompanyName.text = AdminDataHolder.instance.admin!!.companyName
                if (AdminDataHolder.instance.admin!!.image != null) {
                    Glide.with(this).load(AdminDataHolder.instance.admin!!.image).into(binding.ivCompanyLogo)
                }
            }
        }


        // Get Recent products data
        CategoryFirebase.getMainCategoryData { isSuccess, data ->

            if (isSuccess) {
                if (data != null) {

//                    mainCategoryAdapter.updateData(data)
                }
            } else {
                Toast.makeText(this, "Can't retrieve datas.", Toast.LENGTH_LONG).show()
            }
        }


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