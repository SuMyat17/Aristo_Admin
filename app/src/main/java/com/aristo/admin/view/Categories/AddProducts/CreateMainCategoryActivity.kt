package com.aristo.admin.view.Categories.AddProducts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Datas.DataListHolder
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.databinding.ActivityCreateMainCategoryBinding
import com.aristo.admin.view.adapters.MainCategoryListRecyclerViewAdapter

class CreateMainCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMainCategoryBinding
    private val mainCategoryAdapter by lazy { MainCategoryListRecyclerViewAdapter(this, ArrayList()) }
    private val mainCatLayoutManager by lazy { LinearLayoutManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMainCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        DataListHolder.getInstance().deleteAll()
        deleteSharedPreferences()

        binding.createNewCategory.setOnClickListener {
            val intent = Intent(this, AddMainCategoryDetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        binding.mainLoading.visibility = View.VISIBLE

        CategoryFirebase.getMainCategoryData { isSuccess, data ->

            binding.mainLoading.visibility = View.GONE

            if (isSuccess) {
                if (data != null) {

                    mainCategoryAdapter.updateData(data)
                }
            } else {
                Toast.makeText(this, "Can't retrieve datas.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRecyclerView() {
        // Set up the RecyclerView
        binding.rvMainCategory.layoutManager = mainCatLayoutManager
        binding.rvMainCategory.adapter = mainCategoryAdapter
    }

    fun deleteSharedPreferences(){

        SharedPreferencesManager.initialize(this)
        SharedPreferencesManager.removeCategoryId()
        SharedPreferencesManager.removeMainIndex()
    }
}
