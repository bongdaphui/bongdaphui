package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class FieldModel1(
    val id: String? = "",
    val id_city: String? = "",
    val id_district: String? = "",
    val photo: String? = "",
    val name: String? = "",
    val phone: String? = "",
    val address: String? = "",
    val amount_field: String? = "",
    val price: String? = "",
    val id_user_create: String? = ""
) : Serializable