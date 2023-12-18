package com.aristo.admin.model

data class NewProduct (
    var id: String = "",
    val timeStamp: Long = System.currentTimeMillis(),
    var productImage: String? = null,
)