package com.aristo.admin.model

import java.io.Serializable

data class Category(
    var id: String = "",
    val title: String ="",
    val price: Int = 0,
    var imageURL: String = "",
    var new: Boolean = false,
    val colorCode : String = "",
    val type : String = "",
    val timeStamp : Long = System.currentTimeMillis(),
    //var subCategories: ArrayList<Category> = ArrayList()
    var subCategories: Map<String, Category> = mapOf()
): Serializable{
}
