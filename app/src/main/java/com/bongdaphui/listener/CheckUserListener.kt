package com.bongdaphui.listener

interface CheckUserListener {

    fun onCheck(exists: Boolean)

    fun onCancel()
}