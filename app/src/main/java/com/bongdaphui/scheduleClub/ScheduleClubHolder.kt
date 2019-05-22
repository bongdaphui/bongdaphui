package com.bongdaphui.scheduleClub

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bongdaphui.R

class ScheduleClubHolder(row: View) : RecyclerView.ViewHolder(row) {


    var nameClub: TextView = row.findViewById(R.id.item_schedule_tv_name_club)

    var timeStart: TextView = row.findViewById(R.id.item_schedule_time_start)

    var timeEnd: TextView = row.findViewById(R.id.item_schedule_time_end)

    var typeField: TextView = row.findViewById(R.id.item_schedule_type_field)

    var address: TextView = row.findViewById(R.id.item_schedule_tv_address)

}
