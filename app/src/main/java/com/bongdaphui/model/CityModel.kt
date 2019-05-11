package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class CityModel(
    val id: String? = "",
    val name: String? = "",
    val districts: ArrayList<DistrictModel>? = ArrayList()
) : Serializable