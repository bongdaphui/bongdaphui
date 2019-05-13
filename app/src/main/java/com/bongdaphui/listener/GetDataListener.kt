package com.bongdaphui.listener

interface GetDataListener<T> {

    fun onSuccess(list: ArrayList<T>)

    fun onFail(message: String)
}