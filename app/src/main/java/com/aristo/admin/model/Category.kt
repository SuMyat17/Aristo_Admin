package com.aristo.admin.model

import android.net.Uri
import java.io.Serializable

data class Category(
    var id : String = "",
    val title: String,
    val price : Int,
    var imageURL : String,
    val isNew : Boolean,
    var subCategories : ArrayList<Category>
): Serializable{
}
