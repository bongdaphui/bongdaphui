package com.bongdaphui.findClub

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.ScheduleClubModel
import com.bongdaphui.utils.Enum
import com.bumptech.glide.Glide

class FindClubAdapter(
    var context: Context?,
    private val isLoggedUser: Boolean = true,
    private val items: ArrayList<ScheduleClubModel>,
    private var itemClickInterface: OnItemClickListener<ScheduleClubModel>
) : RecyclerView.Adapter<FindClubHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FindClubHolder {

        return FindClubHolder(LayoutInflater.from(context).inflate(R.layout.item_find, p0, false))

    }

    override fun getItemCount(): Int {

        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewPlayerHolder: FindClubHolder, position: Int) {

        val scheduleClubModel: ScheduleClubModel = items[position]

        viewPlayerHolder.timeStart.text = scheduleClubModel.startTime

        viewPlayerHolder.timeEnd.text = scheduleClubModel.endTime

        if (scheduleClubModel.photoUrl?.isNotEmpty()!!) {
            context?.let {
                Glide.with(it).asBitmap().load(scheduleClubModel.photoUrl)
                    .placeholder(context?.resources?.getDrawable(R.drawable.ic_person_grey))
                    .into(viewPlayerHolder.imageView)
            }
        }

        viewPlayerHolder.tvName.text = scheduleClubModel.nameClub

        viewPlayerHolder.tvPhone.text =
            Html.fromHtml("<u> ${if (isLoggedUser) scheduleClubModel.phone else context?.getString(R.string.need_login_to_see)}</u>")

        viewPlayerHolder.tvPhone.setOnClickListener {
            itemClickInterface.onItemClick(scheduleClubModel, position, Enum.EnumTypeClick.Phone.value)
        }

        viewPlayerHolder.containerView.setOnClickListener {
            itemClickInterface.onItemClick(scheduleClubModel, position, Enum.EnumTypeClick.View.value)

        }
    }
}

