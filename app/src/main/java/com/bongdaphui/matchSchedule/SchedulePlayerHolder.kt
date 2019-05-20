package com.bongdaphui.matchSchedule

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bongdaphui.R

class SchedulePlayerHolder(row: View) : RecyclerView.ViewHolder(row) {


    var timeStart: TextView = row.findViewById(R.id.item_schedule_player_time_start)

    var timeEnd: TextView = row.findViewById(R.id.item_schedule_player_time_end)

    var address: TextView = row.findViewById(R.id.item_schedule_player_tv_address)

}
