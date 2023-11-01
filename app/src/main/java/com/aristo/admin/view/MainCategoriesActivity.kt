package com.aristo.admin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.R
import com.aristo.admin.databinding.ActivityMainCategoriesBinding
import com.aristo.admin.databinding.BottomSheetMoreBinding
import com.aristo.admin.model.Category
import com.aristo.admin.model.NewCategory
import com.aristo.admin.view.adapters.MainCategoryListAdapter
import com.aristo.admin.view.adapters.ChildCategoryListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainCategoriesActivity : AppCompatActivity(), MainCategoryListAdapter.MainCategoriesRecyclerViewListener, ChildCategoryListAdapter.ChildCategoryListener {

    private lateinit var binding : ActivityMainCategoriesBinding

    private lateinit var mMainCategoryAdapter: MainCategoryListAdapter
    private lateinit var mSubCategoryAdapter: ChildCategoryListAdapter

    private var categoryList: List<Category> = listOf()
    private var newItemsList: List<NewCategory> = listOf()
    private var currentPosition = 0

    private val categoryListHolder = CategoryDataHolder.getInstance().updatedCategoryList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
//        categoriesViewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]
//        categoriesViewModel.loadCategories()

        setRecyclerViewAdapter()

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        CategoryDataHolder.getInstance().index += 1

        CategoryFirebase.getNewProducts { isSuccess, data ->
            data?.let{newItemsList = it}
        }

        CategoryFirebase.getMainCategoryData { isSuccess, data ->

            if (isSuccess) {
                if (data != null) {
                    categoryList = data

                    if (categoryList.isNotEmpty()){
                        if (categoryListHolder.isEmpty()) {
                            categoryListHolder.add(categoryList[currentPosition])
                        } else {
                            categoryListHolder[0] = categoryList[currentPosition]
                        }

                        mMainCategoryAdapter.setNewData(data)
                        mSubCategoryAdapter.setNewData(data[currentPosition].subCategories.values.toList())
                    }


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
        mSubCategoryAdapter = ChildCategoryListAdapter(this, this, "Main")
        binding.rvSubCategories.layoutManager = GridLayoutManager(this,2)
        binding.rvSubCategories.adapter = mSubCategoryAdapter
    }

    // Reload Sub Categories Recycler View when select main categories recycler view
    override fun reloadSubCategoriesRecyclerView(index : Int) {
        mSubCategoryAdapter.setNewData(categoryList[index].subCategories.values.toList())

        currentPosition = index
        if (categoryListHolder.isNotEmpty()) {
            categoryListHolder[0] = categoryList[currentPosition]
        }
    }

    override fun onTapMore(category: Category, type: String) {

        val newProduct = NewCategory(id = category.id)

        if (type == "Main") {
            val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
            val dialogBinding = BottomSheetMoreBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.show()

//            if (category.new) {
//                dialogBinding.cbNew.isChecked = true
//            }

            newItemsList.forEach {
                if (category.id == it.id) {
                    dialogBinding.cbNew.isChecked = true
                }
            }

            dialogBinding.cbNew.setOnCheckedChangeListener { _, isChecked ->
                category.new = isChecked
                dialogBinding.progressBar.visibility = View.VISIBLE
                dialogBinding.llViews.visibility = View.INVISIBLE

//                CategoryFirebase.updateCategory(category, CategoryDataHolder.getInstance().updatedCategoryList) { _, message -> }

                if (isChecked) {
//                    if (category.subCategories.isEmpty()) {
                        CategoryFirebase.addNewProduct(newProduct) { _, message ->
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                            dialogBinding.progressBar.visibility = View.GONE
                            dialogBinding.llViews.visibility = View.VISIBLE
                            dialog.dismiss()
                        }
//                    }
                } else {
                    CategoryFirebase.removeNewProduct(newProduct) { _, message ->
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        dialogBinding.progressBar.visibility = View.GONE
                        dialogBinding.llViews.visibility = View.VISIBLE
                        dialog.dismiss()
                    }
                }
            }

            dialogBinding.btnDelete.setOnClickListener {
                CategoryFirebase.deleteCategory(category) { _, message ->
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
                CategoryFirebase.removeNewProduct(newProduct) { _, message ->
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }
            }
        }
    }

    override fun onBackPressed() {
        CategoryDataHolder.getInstance().updatedCategoryList.clear()
        CategoryDataHolder.getInstance().index = 0
        super.onBackPressed()
    }
}