package com.aristo.admin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.R
import com.aristo.admin.databinding.ActivityChildCategoriesBinding
import com.aristo.admin.databinding.BottomSheetMoreBinding
import com.aristo.admin.model.Category
import com.aristo.admin.model.NewProduct
import com.aristo.admin.view.adapters.ChildCategoryListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChildCategoriesActivity : AppCompatActivity(), ChildCategoryListAdapter.ChildCategoryListener {

    private lateinit var binding : ActivityChildCategoriesBinding
    private lateinit var mSubCategoryAdapter: ChildCategoryListAdapter
    private var categoryList: List<Category> = listOf()
    private var newItemList: ArrayList<Category> = arrayListOf()
    private var subCategory: Category? = null
    private var pathList: ArrayList<Category> = arrayListOf()
    private var pathIndex: Int = 0

    private val categoryListHolder = CategoryDataHolder.getInstance().updatedCategoryList
    private var isFound = false
    private var isFoundLast = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CategoryDataHolder.getInstance().index += 1

        subCategory = intent.getSerializableExtra("childCategoriesList") as Category?

        setRecyclerViewAdapter()

        subCategory?.let{
            if (categoryListHolder.count() == CategoryDataHolder.getInstance().index) {
                categoryListHolder[CategoryDataHolder.getInstance().index-1] = it
            } else if (categoryListHolder.count() == CategoryDataHolder.getInstance().index-1) {
                categoryListHolder.add(it)
            }
        }

        CategoryFirebase.getMainCategoryData { isSuccess, data ->
            data?.let{categoryList = it}
            data?.forEach { mainCategory ->
                if(!isFound) {
                    findSelectedCategory(mainCategory, subCategory)
                } else {
                    subCategory?.let {
                        mSubCategoryAdapter.setNewData(it.subCategories.values.toList())
                    }
                }
            }
        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun findSelectedCategory(rootCategory: Category, currentCategory: Category?) {
        if (rootCategory.id == currentCategory?.id) {
            isFound = true
            subCategory = rootCategory
        }

        for (subCategory in rootCategory.subCategories.values) {
            findSelectedCategory(subCategory, currentCategory)
        }
    }

    private fun setRecyclerViewAdapter(){
        binding.tvHeading.text = subCategory?.title

        // Sub Categories Recycler View
        mSubCategoryAdapter = ChildCategoryListAdapter(this, this, "Child")
        binding.rvSubCategories.layoutManager = GridLayoutManager(this,2)
        binding.rvSubCategories.adapter = mSubCategoryAdapter

    }

    override fun onBackPressed() {
        CategoryDataHolder.getInstance().index -= 1
        categoryListHolder.removeLast()
        super.onBackPressed()
    }

    override fun onTapMore(category: Category, type: String) {
        val newProduct = NewProduct(id = category.id, title = category.title, price = category.price, imageURL = category.imageURL, new = category.new, colorCode = category.colorCode, type = category.type)

        if (type == "Child") {
            val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
            val binding = BottomSheetMoreBinding.inflate(layoutInflater)
            dialog.setContentView(binding.root)
            dialog.show()

            if (category.new) {
                binding.cbNew.isChecked = true
            }


            binding.cbNew.setOnCheckedChangeListener { _, isChecked ->
                category.new = isChecked

                CategoryFirebase.updateCategory(category, CategoryDataHolder.getInstance().updatedCategoryList) { _, message ->
//                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }

                if (isChecked) {
                    if (category.subCategories.isEmpty()) {
                        CategoryFirebase.addNewProduct(newProduct) { _, message ->
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    CategoryFirebase.removeNewProduct(newProduct) { _, message ->
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                }
                dialog.dismiss()
            }

            binding.btnDelete.setOnClickListener {
                CategoryFirebase.deleteCategory(category) { _, message ->
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
                CategoryFirebase.removeNewProduct(newProduct) { _, message ->
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
                dialog.dismiss()
            }
        }
    }
}