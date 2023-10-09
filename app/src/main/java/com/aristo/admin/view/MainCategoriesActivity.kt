package com.aristo.admin.view

import CategoriesViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.databinding.ActivityMainCategoriesBinding
import com.aristo.admin.model.Category
import com.aristo.admin.view.adapters.MainCategoryListAdapter
import com.aristo.admin.view.adapters.MainCategoriesRecyclerViewListener
import com.aristo.admin.view.adapters.ChildCategoryListAdapter

class MainCategoriesActivity : AppCompatActivity(), MainCategoriesRecyclerViewListener {

    private lateinit var binding : ActivityMainCategoriesBinding

    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var mMainCategoryAdapter: MainCategoryListAdapter
    private lateinit var mSubCategoryAdapter: ChildCategoryListAdapter

    private var categoryList: List<Category> = listOf()

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


        // Get Recent products data
        CategoryFirebase.getMainCategoryData { isSuccess, data ->

            if (isSuccess) {
                if (data != null) {
                    categoryList = data
                    mMainCategoryAdapter.setNewData(data)
                    mSubCategoryAdapter.setNewData(data[0].subCategories.values.toList())

                    Log.d("adfsdfas", "${data[0].subCategories.values.toList()}")
                }
            } else {
                Toast.makeText(this, "Can't retrieve data.", Toast.LENGTH_LONG).show()
            }
            binding.mainLoading.visibility = View.GONE
        }

    }

    private fun setRecyclerViewAdapter(){

        // Main Categories Recycler View
        mMainCategoryAdapter = MainCategoryListAdapter(this, this)
        binding.rvMainCategories.layoutManager = LinearLayoutManager(this)
        binding.rvMainCategories.adapter = mMainCategoryAdapter

        // Sub Categories Recycler View
        mSubCategoryAdapter = ChildCategoryListAdapter(this)
        binding.rvSubCategories.layoutManager = GridLayoutManager(this,2)
        binding.rvSubCategories.adapter = mSubCategoryAdapter
    }

    // Reload Sub Categories Recycler View when select main categories recycler view
    override fun reloadSubCategoriesRecyclerView(index : Int) {
        mSubCategoryAdapter.setNewData(categoryList[index].subCategories.values.toList())
    }
}