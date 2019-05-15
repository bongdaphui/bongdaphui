package com.bongdaphui.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@Entity(tableName = "users")
@IgnoreExtraProperties
data class UserModel(
    @PrimaryKey
    var id: String = "",
    var photoUrl: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var dob: String = "",
    var height: String = "",
    var weight: String = "",
    var position: String = ""
) : Serializable