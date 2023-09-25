package com.smtz.aristo.admin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smtz.aristo.admin.R
import com.smtz.aristo.admin.databinding.ActivityAddMainCategoryDetailBinding

class AddMainCategoryDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddMainCategoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddMainCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreate.setOnClickListener {
            val intent = Intent(this, CreateSubCategoryActivity::class.java)
            startActivity(intent)
        }
    }
}