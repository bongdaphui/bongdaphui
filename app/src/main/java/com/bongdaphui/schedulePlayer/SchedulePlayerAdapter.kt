package com.bongdaphui.schedulePlayer

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

        return SchedulePlayerHolder(LayoutInflater.from(context).inflate(R.layout.item_schedule, p0, false))

    }

    override fun getItemCount(): Int {

        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewPlayerHolder: SchedulePlayerHolder, position: Int) {

        val model: SchedulePlayerModel = items[position]

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

