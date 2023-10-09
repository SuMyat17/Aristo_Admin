package com.aristo.admin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.aristo.admin.databinding.ActivityChildCategoriesBinding
import com.aristo.admin.model.Category
import com.aristo.admin.view.adapters.ChildCategoryListAdapter

class ChildCategoriesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChildCategoriesBinding
    private lateinit var mSubCategoryAdapter: ChildCategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerViewAdapter()

        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    private fun setRecyclerViewAdapter(){
        // Inside your DestinationActivity's onCreate() or wherever you need to access the ArrayList
        val subCategoriesList: ArrayList<Category>? = intent.getSerializableExtra("childCategoriesList") as? ArrayList<Category>

        // Sub Categories Recycler View
        mSubCategoryAdapter = ChildCategoryListAdapter(this)
        binding.rvSubCategories.layoutManager = GridLayoutManager(this,2)
        binding.rvSubCategories.adapter = mSubCategoryAdapter
        if (subCategoriesList != null) {
            mSubCategoryAdapter.setNewData(subCategoriesList)
        }

    }
}