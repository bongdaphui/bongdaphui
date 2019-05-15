package com.bongdaphui.matchSchedule

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bongdaphui.R

class SchedulePlayerHolder(row: View) : RecyclerView.ViewHolder(row) {


    var date: TextView = row.findViewById(R.id.item_match_schedule_tv_date)

    var time: TextView = row.findViewById(R.id.item_match_schedule_tv_time)

}
