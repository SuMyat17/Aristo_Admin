package com.smtz.aristo.admin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smtz.aristo.admin.R
import com.smtz.aristo.admin.databinding.ActivityAddSubCategoryDetailBinding

class AddSubCategoryDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddSubCategoryDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddSubCategoryDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnCreate.setOnClickListener {
            finish()
        }
    }
}