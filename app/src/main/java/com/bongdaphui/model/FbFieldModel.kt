package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class FbFieldModel(
    val id: String? = "",
    val idCity: String? = "",
    val idDistrict: String? = "",
    val photoUrl: String? = "",
    val name: String? = "",
    val phone: String? = "",
    val address: String? = "",
    val amountField: String? = "",
    val price: String? = "",
    val lat: String? = "",
    val lng: String? = "",
    val countRating: String? = "",
    val rating: String? = "",
    val comment: ArrayList<CommentModel>? = ArrayList()
) : Serializable