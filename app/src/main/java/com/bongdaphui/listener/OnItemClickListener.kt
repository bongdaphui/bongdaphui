package com.bongdaphui.listener


interface OnItemClickListener<T> {

    fun onItemClick(item: T, position: Int, type: Int)
}