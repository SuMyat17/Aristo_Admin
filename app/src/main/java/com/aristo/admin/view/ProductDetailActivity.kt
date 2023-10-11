package com.aristo.admin.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aristo.admin.databinding.ActivityProductDetailBinding
import com.aristo.admin.model.Category
import com.bumptech.glide.Glide

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()

        val productDetail: Category? = intent.getSerializableExtra("product") as? Category

        binding.tvTitle.text = productDetail?.title
        if (productDetail?.price != 0) {
            binding.tvPrice.visibility = View.VISIBLE
            binding.tvPrice.text = "စျေးနှုန်း - ${productDetail?.price.toString()} "
        }
        Glide.with(this).load(productDetail?.imageURL).into(binding.ivProduct)

    }

    private fun setUpListeners() {
        binding.ibBack.setOnClickListener {
            super.onBackPressed()
        }
    }

}