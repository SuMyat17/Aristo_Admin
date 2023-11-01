package com.aristo.admin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.R
import com.aristo.admin.databinding.ActivityChildCategoriesBinding
import com.aristo.admin.databinding.BottomSheetMoreBinding
import com.aristo.admin.model.Category
import com.aristo.admin.model.NewCategory
import com.aristo.admin.view.adapters.ChildCategoryListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChildCategoriesActivity : AppCompatActivity(), ChildCategoryListAdapter.ChildCategoryListener {

    private lateinit var binding : ActivityChildCategoriesBinding
    private lateinit var mSubCategoryAdapter: ChildCategoryListAdapter
    private var categoryList: List<Category> = listOf()
    private var newItemsList: List<NewCategory> = listOf()
    private var subCategory: Category? = null
    private val categoryListHolder = CategoryDataHolder.getInstance().updatedCategoryList
    private var isFound = false

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

        CategoryFirebase.getNewProducts { isSuccess, data ->
            data?.let{newItemsList = it}
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

        val newProduct = NewCategory(id = category.id)

        if (type == "Child") {
            val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
            val dialogBinding = BottomSheetMoreBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.show()

            dialogBinding.btnEdit.setOnClickListener {

            }

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
                        CategoryFirebase.addNewProduct(newProduct) { _, message ->
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                            dialogBinding.progressBar.visibility = View.GONE
                            dialogBinding.llViews.visibility = View.VISIBLE
                            dialog.dismiss()
                        }
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
}