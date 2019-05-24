package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class SchedulePlayerModel(
    var id: String? = "",
    var idCity: String? = "",
    var idDistrict: String? = "",
    var startTime: String? = "",
    var endTime: String? = "",
    var idPlayer: String? = "",
    var namePlayer: String? = "",
    var phonePlayer: String? = "",
    var photoUrlPlayer: String? = "",
    var typeField: String? = ""


)