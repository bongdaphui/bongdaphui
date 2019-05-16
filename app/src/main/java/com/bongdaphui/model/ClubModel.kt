package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class ClubModel(
    val id: String? = "",
    val idCaptain: String? = "",
    val photo: String? = "",
    val name: String? = "",
    val caption: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val dob: String? = "",
    val address: String? = "",
    val idDistrict: String? = "",
    val idCity: String? = "",
    val matchWin: String? = "",
    val matchLose: String? = "",
    val countRating: String? = "",
    val rating: String? = "",
    val players: ArrayList<String> = ArrayList()

) : Serializable {

    fun getAmountPlayer(): Int {
        return players.size
    }
}