package com.bongdaphui.utils

import android.content.Context
import android.content.SharedPreferences
import com.bongdaphui.base.BaseApplication
import kotlin.coroutines.coroutineContext

class SharePreferenceManager private constructor(context: Context){
    var prefs:SharedPreferences? =null
    private val KEY_SHARED_PREFERENCES = Utils().getSharedPreferenceKey()
    init {
        prefs = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    private object Holder {

        var INSTANCE : SharePreferenceManager? = null}



    companion object {
        @JvmStatic
        fun getInstance(context: Context): SharePreferenceManager{
            if (Holder.INSTANCE == null) {
                Holder.INSTANCE = SharePreferenceManager(context)
            }
            return Holder.INSTANCE!!
        }
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