package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class RequestJoinClubModel(
    val id: String? = "",
    val idClub: String? = "",
    val user: UserModel = UserModel()

)