package com.bongdaphui.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "fields")
data class FbFieldModel(
    @PrimaryKey
    @NonNull var id: Long = 0,
    var idCity: String? = "",
    var idDistrict: String? = "",
    var photoUrl: String? = "",
    var name: String? = "",
    var phone: String? = "",
    var phone2: String? = "",
    var address: String? = "",
    var amountField: String? = "",
    var price: String? = "",
    var priceMax: String? = "",
    var lat: String? = "",
    var lng: String? = "",
    var countRating: String? = "",
    var rating: String? = ""
)