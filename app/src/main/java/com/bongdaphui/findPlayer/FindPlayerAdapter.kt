package com.bongdaphui.findPlayer

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.SchedulePlayerModel
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide

class FindPlayerAdapter(
    var context: Context?,
    private val isLoggedUser: Boolean = true,
    private val items: ArrayList<SchedulePlayerModel>,
    private var itemClickInterface: OnItemClickListener<SchedulePlayerModel>
) : RecyclerView.Adapter<FindPlayerHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FindPlayerHolder {

        return FindPlayerHolder(LayoutInflater.from(context).inflate(R.layout.item_find, p0, false))

    }

    override fun getItemCount(): Int {

        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewPlayerHolder: FindPlayerHolder, position: Int) {

        val model: SchedulePlayerModel = items[position]

        viewPlayerHolder.timeStart.text = model.startTime

        viewPlayerHolder.timeEnd.text = model.endTime

        viewPlayerHolder.typeField.text = "Loại sân: ${model.typeField?.let { Utils().getTypeField(it) }}"

        if (model.photoUrlPlayer?.isNotEmpty()!!) {
            context?.let {
                Glide.with(it).asBitmap().load(model.photoUrlPlayer)
                    .placeholder(context?.resources?.getDrawable(R.drawable.ic_person_grey))
                    .into(viewPlayerHolder.imageView)
            }
        }

        viewPlayerHolder.tvName.text = model.namePlayer

        viewPlayerHolder.tvPhone.text =
            if (isLoggedUser) model.phonePlayer else context?.getString(R.string.need_login_to_see)

        viewPlayerHolder.call.setOnClickListener {
            itemClickInterface.onItemClick(model, position, Enum.EnumTypeClick.Phone.value)
        }

        viewPlayerHolder.containerView.setOnClickListener {
            itemClickInterface.onItemClick(model, position, Enum.EnumTypeClick.View.value)

        }
    }
}

