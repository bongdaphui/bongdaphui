package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class ScheduleClubModel(
    val id: String? = "",
    val idClub: String? = "",
    val idCaptain: String? = "",
    val idCity: String? = "",
    val idDistrict: String? = "",
    val startTime: String? = "",
    val endTime: String? = "",
    val nameClub: String? = "",
    val phone: String? = "",
    val photoUrl: String? = "",
    val typeField: String? = ""
)