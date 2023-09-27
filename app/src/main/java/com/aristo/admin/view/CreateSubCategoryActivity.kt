package com.aristo.admin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aristo.admin.databinding.ActivityCreateSubCategoryBinding

class CreateSubCategoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateSubCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateSubCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setRecyclerViewAdapter()

        binding.createSubCategory.setOnClickListener {
            val intent = Intent(this, AddSubCategoryDetailActivity::class.java)
            startActivity(intent)
        }
    }

    fun setRecyclerViewAdapter(){

        // Sub Categories Recycler View
        val subCatLayoutManager = LinearLayoutManager(this)
        binding.rvSubCategory.layoutManager = subCatLayoutManager
        binding.rvSubCategory.adapter = SubCategoryListRecyclerViewAdapter(this)
    }
}