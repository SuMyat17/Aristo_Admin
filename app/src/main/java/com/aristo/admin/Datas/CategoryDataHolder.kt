package com.aristo.admin.Datas

import com.aristo.admin.model.Category

class CategoryDataHolder private constructor() {

    val updatedCategoryList: ArrayList<Category> = ArrayList()
    var index: Int = 0

    var countingType: String = ""


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
