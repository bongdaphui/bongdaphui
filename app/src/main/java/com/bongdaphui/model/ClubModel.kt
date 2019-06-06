package com.bongdaphui.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class ClubModel(
    var id: String = "",
    var idCaptain: String = "",
    var photo: String = "",
    var name: String = "",
    var caption: String = "",
    var email: String = "",
    var phone: String = "",
    var dob: String = "",
    var address: String = "",
    var idDistrict: String = "",
    var idCity: String = "",
    var matchWin: String = "",
    var matchLose: String = "",
    var countRating: String = "",
    var rating: String = "",
    var players: ArrayList<String> = ArrayList(),
    var comments: ArrayList<String> = ArrayList()

) : Serializable {

    fun getAmountPlayer(): Int {
        return players.size
    }
}