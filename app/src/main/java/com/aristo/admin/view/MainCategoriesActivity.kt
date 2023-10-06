package com.aristo.admin.view

import CategoriesViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aristo.admin.databinding.ActivityMainCategoriesBinding
import com.aristo.admin.view.adapters.MainCategoryListAdapter
import com.aristo.admin.view.adapters.MainCategoriesRecyclerViewListener
import com.aristo.admin.view.adapters.SubCategoryListAdapter

class MainCategoriesActivity : AppCompatActivity(), MainCategoriesRecyclerViewListener {

    private lateinit var binding : ActivityMainCategoriesBinding

    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var mMainCategoryAdapter: MainCategoryListAdapter
    private lateinit var mSubCategoryAdapter: SubCategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        categoriesViewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]
        categoriesViewModel.loadCategories()

        setRecyclerViewAdapter()

        binding.ibBack.setOnClickListener {
            finish()
        }

    }

    private fun setRecyclerViewAdapter(){

        // Main Categories Recycler View
        mMainCategoryAdapter = MainCategoryListAdapter(this, categoriesViewModel.dummyDataList)
        binding.rvMainCategories.layoutManager = LinearLayoutManager(this)
        binding.rvMainCategories.adapter = mMainCategoryAdapter

        // Sub Categories Recycler View
//        mSubCategoryAdapter = SubCategoryListAdapter(this, categoriesViewModel.mainCategoryList[0].subCategories)
//        mSubCategoryAdapter = SubCategoryListAdapter(this, categoriesViewModel.dummyDataList[0].subCategories)
//        binding.rvSubCategories.layoutManager = GridLayoutManager(this,2)
//        binding.rvSubCategories.adapter = mSubCategoryAdapter
    }

    // Reload Sub Categories Recycler View when select main categories recycler view
    override fun reloadSubCategoriesRecyclerView(index : Int) {
        //binding.rvSubCategories.adapter = SubCategoryListAdapter(this, categoriesViewModel.dummyDataList[index].subCategories)
    }
}