package com.bongdaphui.scheduleClub

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.model.ScheduleClubModel
import com.bongdaphui.utils.Utils

class ScheduleClubAdapter(
    var context: Context?,
    private val items: ArrayList<ScheduleClubModel>
) : RecyclerView.Adapter<ScheduleClubHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ScheduleClubHolder {

        return ScheduleClubHolder(LayoutInflater.from(context).inflate(R.layout.item_schedule, p0, false))

    }

    override fun getItemCount(): Int {

        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewPlayerHolder: ScheduleClubHolder, position: Int) {

        val scheduleClubModel: ScheduleClubModel = items[position]

        viewPlayerHolder.nameClub.visibility = View.VISIBLE

        viewPlayerHolder.nameClub.text = scheduleClubModel.nameClub

        viewPlayerHolder.timeStart.text = scheduleClubModel.startTime

        viewPlayerHolder.timeEnd.text = scheduleClubModel.endTime

        viewPlayerHolder.address.text =
            context?.let { scheduleClubModel.idCity?.let { it1 ->
                scheduleClubModel.idDistrict?.let { it2 ->
                    Utils().getAddress(it,
                        it1, it2
                    )
                }
            } }

    }


}

