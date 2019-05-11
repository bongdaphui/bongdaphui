package com.bongdaphui.listener


interface ItemClickInterface<T> {

    fun OncItemlick(item: T, position: Int, type: Int)
}