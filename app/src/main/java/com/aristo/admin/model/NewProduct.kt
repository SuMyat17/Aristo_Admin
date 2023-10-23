package com.aristo.admin.model

import java.io.Serializable

data class NewProduct(
    var id: String = "",
    val title: String ="",
    val price: Int = 0,
    var imageURL: String = "",
    var new: Boolean = false,
    val colorCode : String = "",
    val type : String = "",
    var timeStamp : Long = System.currentTimeMillis()
): Serializable{
}
