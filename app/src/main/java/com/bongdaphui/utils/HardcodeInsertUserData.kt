package com.bongdaphui.utils

import android.util.Log
import com.bongdaphui.model.UserModel
import com.google.firebase.database.FirebaseDatabase

class HardcodeInsertUserData {

    fun addData() {

        val databaseReference = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_USER)

        val userModel = UserModel("GwpjEYwv5XgYPfd1BrRslIOzxuq2", "", "", "", "", "")

        databaseReference

            .child("${userModel.id}").setValue(userModel)

            .addOnCompleteListener {

                Log.d(Constant().TAG, "add user success ")

            }
            .addOnFailureListener {

                Log.d(Constant().TAG, "add field user : $it")

            }
    }
}