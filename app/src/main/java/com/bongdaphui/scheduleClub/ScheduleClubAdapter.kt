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

        val model: ScheduleClubModel = items[position]

        viewPlayerHolder.nameClub.visibility = View.VISIBLE

        viewPlayerHolder.nameClub.text = model.nameClub

        viewPlayerHolder.timeStart.text = model.startTime

        viewPlayerHolder.timeEnd.text = model.endTime

        viewPlayerHolder.typeField.text = model.typeField?.let { Utils().getTypeField(it) }

        viewPlayerHolder.address.text =
            context?.let {
                model.idCity?.let { it1 ->
                    model.idDistrict?.let { it2 ->
                        Utils().getNameCityDistrictFromId(
                            it,
                            it1, it2
                        )
                    }
                }
            }

    }


}

