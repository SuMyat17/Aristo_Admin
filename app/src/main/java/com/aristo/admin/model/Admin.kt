package com.aristo.admin.model

import java.io.Serializable

data class Admin (
    var companyName: String = "",
    var address: String = "",
    var phone: String = "",
    var image: String? = null,
    var viber: String? = null,
    var fbPage: String? = null,
    var fbPageLink: String? = null,
) : Serializable