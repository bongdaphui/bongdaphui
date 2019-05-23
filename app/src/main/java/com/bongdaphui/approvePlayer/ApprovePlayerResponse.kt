package com.bongdaphui.approvePlayer

/**
 * Created by ChuTien on ${1/25/2017}.
 */
data class ApprovePlayerResponse (
    var idClub : String ="",
    var nameClub : String ="",
    var idPlayer:String = "",
    var photoPlayer:String = "",
    var namePlayer:String = "",
    var idCaptain:String = "",
    var isAccepted:Int = 2 //2 is pending 1 is approved 0 is rejected
)