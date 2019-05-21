package com.bongdaphui.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@Entity(tableName = "users")
@IgnoreExtraProperties
data class UserStickModel(
    @PrimaryKey
    @NonNull var id: String = "",
    var photoUrl: String = "",
    var name: String = "",
    var position: String = ""
) : Serializable