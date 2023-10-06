package com.aristo.admin.Manager
import android.content.Context
import android.content.SharedPreferences
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.preference.PreferenceManager

object SharedPreferencesManager {
    private const val PREF_NAME = "SharedPreferencesForCategory"
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveCategoryId(categoryId: String) {
        val editor = sharedPreferences.edit()
        editor.putString("categoryId", categoryId)
        editor.apply()
    }

    fun getCategoryId(): String? {
        return sharedPreferences.getString("categoryId", null)
    }

    fun isSaveSubCategory(isSave: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isSave", isSave)
        editor.apply()
    }

    fun getIsSaveSubCategory(): Boolean? {
        return sharedPreferences.getBoolean("isSave", false)
    }

//    fun saveSubCategoryId(subCategoryId: String) {
//        val editor = sharedPreferences.edit()
//        editor.putString("subCategoryId", subCategoryId)
//        editor.apply()
//    }
//
//    fun getSubCategoryId(): String? {
//        return sharedPreferences.getString("subCategoryId", "")
//    }

    fun saveMainIndex(index: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("position", index)
        editor.apply()
    }

    fun getMainIndex(): Int {
        return sharedPreferences.getInt("position", 0)
    }

    fun customPrefs(context: Context, name: String) : SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    fun SharedPreferences.set(key: String, value: String?) {
        edit {
            it.putString(key, value)
        }
    }

    fun SharedPreferences.get(key: String) : String? {
        return getString(key, null)
    }
}
