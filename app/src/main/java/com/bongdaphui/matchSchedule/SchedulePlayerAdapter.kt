package com.bongdaphui.matchSchedule

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.model.SchedulePlayerModel

class SchedulePlayerAdapter(
    var context: Context?,
    private val items: ArrayList<SchedulePlayerModel>
) : RecyclerView.Adapter<SchedulePlayerHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SchedulePlayerHolder {

        return SchedulePlayerHolder(LayoutInflater.from(context).inflate(R.layout.item_schedule_player, p0, false))

    }

    override fun getItemCount(): Int {

        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewPlayerHolder: SchedulePlayerHolder, position: Int) {

        val schedulePlayerModel: SchedulePlayerModel = items[position]


    }

}

