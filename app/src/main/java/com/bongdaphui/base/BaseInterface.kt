package com.bongdaphui.base

import android.support.annotation.AnimRes
import android.support.v4.app.FragmentActivity

interface BaseInterface {

    fun onBindView()

    @AnimRes
    fun getEnterAnimation(): Int

    abstract fun getActiveActivity(): FragmentActivity

}