package com.bongdaphui.model

import android.arch.persistence.room.Entity
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@Entity(tableName = "users")
@IgnoreExtraProperties
data class UserModel(
    val id: String? = "",
    val photoUrl: String? = "",
    val name: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val dob: String? = "",
    val height: String? = "",
    val weight: String? = "",
    val position: String? = ""
) : Serializable