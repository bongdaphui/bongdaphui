package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class SchedulePlayerModel(
    val id: String? = "",
    val idCity: String? = "",
    val idDistrict: String? = "",
    val startTime: String? = "",
    val endTime: String? = "",
    val idPlayer: String? = "",
    val namePlayer: String? = "",
    val phonePlayer: String? = "",
    val photoUrlPlayer: String? = "",
    val typeField: String? = ""


)