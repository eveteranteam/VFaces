package ua.gov.mva.vfaces.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object Preferences {

    private lateinit var prefs: SharedPreferences

    @Synchronized
    fun init(appContext: Context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    @Synchronized
    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    @Synchronized
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }

    @Synchronized
    fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    @Synchronized
    fun getString(key: String, defValue: String): String? {
        return prefs.getString(key, defValue)
    }

    @Synchronized
    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}