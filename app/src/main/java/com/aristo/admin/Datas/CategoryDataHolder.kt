package com.aristo.admin.Datas

import com.aristo.admin.model.Category

class CategoryDataHolder private constructor() {

    private val updatedCategoryList: ArrayList<Category> = ArrayList()
    private val childCategoryList : ArrayList<Category> = ArrayList()


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
