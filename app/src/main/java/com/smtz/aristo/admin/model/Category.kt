package com.smtz.aristo.admin.model

import java.io.Serializable

data class Category(
    val id: Int,
    val title: String,
    val subCategories: ArrayList<Category>
): Serializable
