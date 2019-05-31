package com.bongdaphui.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
@Entity(tableName = "fields")
data class FbFieldModel(
    @PrimaryKey
    @NonNull val id: Long?,
    val idCity: String? = "",
    val idDistrict: String? = "",
    val photoUrl: String? = "",
    var name: String? = "",
    val phone: String? = "",
    val phone2: String? = "",
    val address: String? = "",
    val amountField: String? = "",
    val price: String? = "",
    val priceMax: String? = "",
    val lat: String? = "",
    val lng: String? = "",
    val countRating: String? = "",
    val rating: String? = ""
) : Serializable