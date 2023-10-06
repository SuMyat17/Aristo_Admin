package com.aristo.admin.Datas

import android.util.Log

class DataListHolder private constructor() {

    private val subIndexList: ArrayList<Int> = ArrayList()
    private val subIDList: ArrayList<String> = ArrayList()
    private val childList: ArrayList<String> = ArrayList()
    fun setSubIndexList(data: Int) {

        Log.d("setSubIndexList", "setSubIndexList: $data")
        //subIndexList.clear()
        subIndexList.add(data)
    }

    fun getSubIndexList(): ArrayList<Int> {
        return subIndexList
    }

    fun setSubIDList(data: String) {

        subIDList.add(data)
    }

    fun getSubIDList(): ArrayList<String> {
        return subIDList
    }

    fun setChildPath(data: String) {

        childList.add(data)
    }

    fun getChildPath(): ArrayList<String> {
        return childList
    }

    fun deleteAll(){
        subIDList.clear()
        subIndexList.clear()
    }


    companion object {
        private var instance: DataListHolder? = null

        fun getInstance(): DataListHolder {
            if (instance == null) {
                instance = DataListHolder()
            }
            return instance!!
        }
    }
}
