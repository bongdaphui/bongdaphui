package com.bongdaphui.listener

/**
 * Created by ChuTien on ${1/25/2017}.
 */
interface OnApproveListener<T> {
    fun onAccepted(item: T)
    fun onRejected(item: T)
}