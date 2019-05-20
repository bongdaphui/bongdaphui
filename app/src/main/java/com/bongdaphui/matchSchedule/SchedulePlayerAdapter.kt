package com.bongdaphui.matchSchedule

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.model.SchedulePlayerModel
import com.bongdaphui.utils.Utils

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

        viewPlayerHolder.timeStart.text = schedulePlayerModel.startTime

        viewPlayerHolder.timeEnd.text = schedulePlayerModel.endTime

        viewPlayerHolder.address.text =
            context?.let { schedulePlayerModel.idCity?.let { it1 ->
                schedulePlayerModel.idDistrict?.let { it2 ->
                    Utils().getAddress(it,
                        it1, it2
                    )
                }
            } }

    }


}

