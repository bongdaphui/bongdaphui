package com.bongdaphui.base

import android.annotation.SuppressLint
import android.content.Context
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage

@SuppressLint("Registered")
class BaseApplication : MultiDexApplication() {

    private var mContext: Context? = null
    private var mActiveActivity: AppCompatActivity? = null


    fun getContext(): Context? {
        return mContext
    }


    fun getActiveActivity(): AppCompatActivity? {
        return mActiveActivity
    }

    fun setActiveActivity(active: AppCompatActivity) {
        mActiveActivity = active
    }


    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext

        FirebaseApp.initializeApp(applicationContext)
        FirebaseStorage.getInstance().maxOperationRetryTimeMillis = 1000

    }

}