package com.bongdaphui.findPlayer

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.SchedulePlayerModel
import com.bongdaphui.utils.DateTimeUtil
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
    override fun onBindViewHolder(holder: FindPlayerHolder, position: Int) {

        val model: SchedulePlayerModel = items[position]

        holder.timeStart.text = model.startTime?.let {
            DateTimeUtil().getFormatDate(
                it,
                DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format
            )
        }

        holder.timeEnd.text = model.endTime?.let {
            DateTimeUtil().getFormatDate(
                it,
                DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format
            )
        }

        holder.typeField.text = "${model.typeField?.let { Utils().getTypeField(it) }}"

        holder.area.text =
            "${context?.let {
                model.idCity.let { it1 ->
                    it1?.let { it2 ->
                        Utils().getNameCityDistrictFromId(
                            it,
                            it2, model.idDistrict
                        )
                    }
                }
            }}"

        if (!TextUtils.isEmpty(model.photoUrlPlayer)) {
            context?.let {
                Glide.with(it).asBitmap().load(model.photoUrlPlayer).placeholder(R.drawable.ic_person_grey)
                    .into(holder.imageView)
            }
        } else {
            holder.imageView.setImageResource(R.drawable.ic_person_grey)
        }

        holder.tvName.text = model.namePlayer

        holder.tvPhone.text =
            if (isLoggedUser) model.phonePlayer else context?.getString(R.string.need_login_to_see)

        holder.call.setOnClickListener {
            itemClickInterface.onItemClick(model, position, Enum.EnumTypeClick.Phone.value)
        }

        holder.containerView.setOnClickListener {
            itemClickInterface.onItemClick(model, position, Enum.EnumTypeClick.View.value)

        }
    }
}

