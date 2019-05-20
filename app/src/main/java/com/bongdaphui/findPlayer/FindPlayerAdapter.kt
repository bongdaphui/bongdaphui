package com.bongdaphui.findPlayer

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.SchedulePlayerModel
import com.bongdaphui.utils.Enum
import com.bumptech.glide.Glide

class FindPlayerAdapter(
    var context: Context?,
    private val isLoggedUser: Boolean = true,
    private val items: ArrayList<SchedulePlayerModel>,
    private var itemClickInterface: OnItemClickListener<SchedulePlayerModel>
) : RecyclerView.Adapter<FindPlayerHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FindPlayerHolder {

        return FindPlayerHolder(LayoutInflater.from(context).inflate(R.layout.item_find_player, p0, false))

    }

    override fun getItemCount(): Int {

        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewPlayerHolder: FindPlayerHolder, position: Int) {

        val schedulePlayerModel: SchedulePlayerModel = items[position]

        viewPlayerHolder.timeStart.text = schedulePlayerModel.startTime

        viewPlayerHolder.timeEnd.text = schedulePlayerModel.endTime

        if (schedulePlayerModel.photoUrlPlayer!!.isNotEmpty()) {
            Glide.with(context!!).asBitmap().load(schedulePlayerModel.photoUrlPlayer)
                .into(viewPlayerHolder.imageView)
        }

        viewPlayerHolder.tvName.text = schedulePlayerModel.namePlayer

        viewPlayerHolder.tvPhone.text =
            Html.fromHtml("<u> ${if (isLoggedUser) schedulePlayerModel.phonePlayer else context!!.getString(R.string.need_login_to_see)}</u>")

        viewPlayerHolder.tvPhone.setOnClickListener {
            itemClickInterface.onItemClick(schedulePlayerModel, position, Enum.EnumTypeClick.Phone.value)
        }

        viewPlayerHolder.containerView.setOnClickListener {
            itemClickInterface.onItemClick(schedulePlayerModel, position, Enum.EnumTypeClick.View.value)

        }
    }
}

