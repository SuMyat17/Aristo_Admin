package com.aristo.admin.Datas

import com.aristo.admin.model.Category

class CategoryDataHolder private constructor() {

    private val updatedCategoryList: ArrayList<Category> = ArrayList()
    private val childCategoryList : ArrayList<Category> = ArrayList()

    fun setUpdatedCategoryList(data: ArrayList<Category>) {

        updatedCategoryList.clear()
        updatedCategoryList.addAll(data)
    }

    fun getUpdatedCategoryList(): ArrayList<Category> {
        return updatedCategoryList
    }

    fun setChildCategory(data: Category) {

        childCategoryList.clear()
        childCategoryList.add(data)
    }

    fun getChildCategoryList(): ArrayList<Category> {
        return childCategoryList
    }

    companion object {
        private var instance: CategoryDataHolder? = null

        fun getInstance(): CategoryDataHolder {
            if (instance == null) {
                instance = CategoryDataHolder()
            }
            return instance!!
        }
    }
}
