package com.bongdaphui.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.bongdaphui.utils.DateTimeUtil

@Entity(tableName = "schedulePlayer")
data class SchedulePlayerModel(
    @PrimaryKey
    @NonNull var id: String = "",
    var idCity: String? = "",
    var idDistrict: String? = "",
    var startTime: String? = "",
    var endTime: String? = "",
    var idPlayer: String? = "",
    var namePlayer: String? = "",
    var phonePlayer: String? = "",
    var photoUrlPlayer: String? = "",
    var typeField: String? = ""
) {
    fun getTimeInMillisStart(): Long {
        return startTime?.let {
            DateTimeUtil().getTimeInMilliseconds(
                it,
                DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format
            )
        }!!
    }
}