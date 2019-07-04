package com.example.byheart.shared

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private var pref: SharedPreferences? = null
    val FIRST_START = "FIRST_START"
    val DARK_MODE = "DARK_MODE"
    val REHEARSAL_REVERSE = "REHEARSAL_REVERSE"
    val REHEARSAL_SHUFFLE = "REHEARSAL_SHUFFLE"
    val REHEARSAL_TYPED = "REHEARSAL_TYPED"
    val REHEARSAL_MULTIPLE_CHOICE = "REHEARSAL_MULTIPLE_CHOICE"
    val REHEARSAL_MEMORY = "REHEARSAL_MEMORY"
    val REHEARSAL_PRONOUNCE = "REHEARSAL_PRONOUNCE"

    fun init(context: Context) {
        if (pref == null)
            pref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun read(key: String, default: String): String? {
        return pref!!.getString(key.toUpperCase(), default)
    }

    fun write(key: String, value: String) {
        pref!!.edit().putString(key.toUpperCase(), value).apply()
    }

    fun read(key: String, default: Boolean = false): Boolean {
        return pref!!.getBoolean(key.toUpperCase(), default)
    }

    fun write(key: String, value: Boolean) {
        pref!!.edit().putBoolean(key.toUpperCase(), value).apply()
    }

    fun read(key: String, default: Int): Int {
        return pref!!.getInt(key.toUpperCase(), default)
    }

    fun write(key: String, value: Int?) {
        pref!!.edit().putInt(key.toUpperCase(), value!!).apply()
    }

    fun toggle(key:String) {
        pref!!.edit().putBoolean(key.toUpperCase(), !read(key, false)).apply()
    }
}