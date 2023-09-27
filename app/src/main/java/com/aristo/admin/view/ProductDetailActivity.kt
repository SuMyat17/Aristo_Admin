package com.aristo.admin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aristo.admin.databinding.ActivityProductDetailBinding
import com.aristo.admin.model.Category

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()
        setUpData()
    }

    private fun setUpListeners() {
        binding.ibBack.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setUpData() {
        val productDetail: Category? = intent.getSerializableExtra("product") as? Category

        binding.tvCartTitle.text = productDetail?.title
    }
}