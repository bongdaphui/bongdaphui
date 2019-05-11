package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class DistrictModel(
    val id: String? = "",
    val name: String? = ""
//    val fields: ArrayList<FbFieldModel>? = ArrayList()
) : Serializable