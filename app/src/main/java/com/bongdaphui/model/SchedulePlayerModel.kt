package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class SchedulePlayerModel(
    val idCity: String? = "",
    val idDistrict: String? = "",
    val startTime: String? = "",
    val endTime: String? = "",
    val idPlayer: String? = "",
    val namePlayer: String? = "",
    val photoUrlPlayer: String? = ""
)