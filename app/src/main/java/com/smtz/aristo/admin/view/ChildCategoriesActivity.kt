package com.smtz.aristo.admin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.smtz.aristo.admin.databinding.ActivityChildCategoriesBinding
import com.smtz.aristo.admin.model.Category
import com.smtz.aristo.admin.view.adapters.ChildCategoryListAdapter

class ChildCategoriesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChildCategoriesBinding

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
        val childCategoriesList: ArrayList<Category>? = intent.getSerializableExtra("childCategoriesList") as? ArrayList<Category>

        // Sub Categories Recycler View
        val subCatLayoutManager = GridLayoutManager(this,2)
        binding.rvSubCategories.layoutManager = subCatLayoutManager
        binding.rvSubCategories.adapter = childCategoriesList?.let {
            ChildCategoryListAdapter(this, it)
        }
    }
}