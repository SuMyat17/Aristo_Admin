package com.aristo.admin.Datas

import com.aristo.admin.model.Admin

class AdminDataHolder private constructor(){

    var admin : Admin? = null

    companion object {
        val instance: AdminDataHolder = AdminDataHolder()
    }

}