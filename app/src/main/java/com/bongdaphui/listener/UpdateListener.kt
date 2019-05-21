package com.bongdaphui.listener

interface UpdateListener {

    fun onUpdateSuccess()

    fun onUpdateFail(err : String ="")

}