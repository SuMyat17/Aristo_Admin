package com.smtz.aristo.admin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smtz.aristo.admin.R
import com.smtz.aristo.admin.databinding.ActivityCreateMainCategoryBinding

class CreateMainCategoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateMainCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMainCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setRecyclerViewAdapter()

        binding.createNewCategory.setOnClickListener {
            val intent = Intent(this,AddMainCategoryDetailActivity::class.java)
            startActivity(intent)
        }
    }

    fun setRecyclerViewAdapter(){

        // Sub Categories Recycler View
        val mainCatLayoutManager = LinearLayoutManager(this)
        binding.rvMainCategory.layoutManager = mainCatLayoutManager
        binding.rvMainCategory.adapter = MainCategoryListRecyclerViewAdapter(this)
    }
}