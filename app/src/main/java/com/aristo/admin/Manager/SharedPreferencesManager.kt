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

    fun removeCategoryId() {
        val editor = sharedPreferences.edit()
        editor.remove("categoryId")
        editor.apply()
    }


    fun saveMainIndex(index: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("position", index)
        editor.apply()
    }

    fun getMainIndex(): Int {
        return sharedPreferences.getInt("position", 0)
    }

    // Function to remove categoryId from SharedPreferences
    // Function to remove position from SharedPreferences
    fun removeMainIndex() {
        val editor = sharedPreferences.edit()
        editor.remove("position")
        editor.apply()
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
