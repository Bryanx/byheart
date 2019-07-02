package com.example.byheart.shared

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private var pref: SharedPreferences? = null
    val FIRST_START = "FIRST_START"
    val DARK_MODE = "DARK_MODE"

    fun init(context: Context) {
        if (pref == null)
            pref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun read(key: String, default: String): String? {
        return pref!!.getString(key, default)
    }

    fun write(key: String, value: String) {
        pref!!.edit().putString(key, value).apply()
    }

    fun read(key: String, default: Boolean): Boolean {
        return pref!!.getBoolean(key, default)
    }

    fun write(key: String, value: Boolean) {
        pref!!.edit().putBoolean(key, value).apply()
    }

    fun read(key: String, default: Int): Int {
        return pref!!.getInt(key, default)
    }

    fun write(key: String, value: Int?) {
        pref!!.edit().putInt(key, value!!).apply()
    }

    fun toggle(key:String) {
        pref!!.edit().putBoolean(key, !read(key, false)).apply()
    }
}