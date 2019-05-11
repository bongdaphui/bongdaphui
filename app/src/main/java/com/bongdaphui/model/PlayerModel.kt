package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PlayerModel(
    val id: String? = "",
    val idUser: String? = "",
    val idClub: String? = "",
    val photo: String? = "",
    val name: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val dob: String? = "",
    val address: String? = "",
    val height: String? = "",
    val weight: String? = "",
    val position: String? = "",
    val clothesNumber: String? = "",
    val comments: ArrayList<CommentModel> = ArrayList()

)