package com.aristo.admin.view.Categories.AddProducts

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Datas.DataListHolder
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

        binding.createNewCategory.setOnClickListener {
            val intent = Intent(this, AddMainCategoryDetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Retrieve the updated data from DataHolder
        val updatedCategoryList = CategoryDataHolder.getInstance().getUpdatedCategoryList()
        Toast.makeText(this, "Category ${updatedCategoryList.size}", Toast.LENGTH_LONG).show()
        DataListHolder.getInstance().deleteAll()
        // Update the adapter with the new data
        mainCategoryAdapter.updateData(updatedCategoryList)
    }

    private fun setupRecyclerView() {
        // Set up the RecyclerView
        binding.rvMainCategory.layoutManager = mainCatLayoutManager
        binding.rvMainCategory.adapter = mainCategoryAdapter
    }
}
