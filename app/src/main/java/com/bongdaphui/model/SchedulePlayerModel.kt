package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class SchedulePlayerModel(
    val id: String? = "",
    val date: String? = "",
    val time: String? = "",
    val idCityArea: String? = "",
    val idDistrictArea: String? = "",
    val userModel: UserModel
)