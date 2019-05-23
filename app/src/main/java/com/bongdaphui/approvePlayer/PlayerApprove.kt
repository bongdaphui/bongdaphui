package com.bongdaphui.approvePlayer

import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import java.io.Serializable

/**
 * Created by ChuTien on ${1/25/2017}.
 */
data class PlayerApprove(
    @NonNull var id: String = "",
    var photoUrl: String = "",
    var name: String = "",
    var message: String = "",
    var idClub: String = "",
    var section: Boolean = false
)