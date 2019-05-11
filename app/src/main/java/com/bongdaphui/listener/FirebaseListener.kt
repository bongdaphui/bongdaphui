package com.bongdaphui.listener

import com.google.firebase.database.DataSnapshot

interface FireBaseSuccessListener {

    fun onSuccess(data: DataSnapshot)

    fun onFail(message: String)
}