package com.bongdaphui.listener

interface GetDataListener<T> {

    fun onSuccess(list: ArrayList<T>)

    fun onSuccess(item :T)

    fun onFail(message: String)
}