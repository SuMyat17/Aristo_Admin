package com.aristo.admin.model

import java.io.Serializable

data class User (

    val address : String = "",
    val password : String = "",
    val phone : String = "",
    val point : Int = 0,
    val userId : Long = 0,
    val userName : String = ""
): Serializable {
}