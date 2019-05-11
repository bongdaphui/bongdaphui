package com.bongdaphui.utils

import android.content.Context
import com.bongdaphui.base.BaseApplication

class SharePreferenceManager {

    private lateinit var instance: SharePreferenceManager

    private val KEY_SHARED_PREFERENCES = Utils().getSharedPreferenceKey()

    val prefs = BaseApplication().getContext()!!.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

    @Synchronized
    fun getInstance(): SharePreferenceManager {
        return instance
    }


    @Synchronized
    fun setString(key: String, value: String): Boolean {
        return prefs?.edit()?.putString(key, value)?.commit() ?: false
    }

    @Synchronized
    fun getString(key: String): String? {
        return prefs?.getString(key, "")
    }
}