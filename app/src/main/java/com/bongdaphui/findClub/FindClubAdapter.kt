package com.bongdaphui.findClub

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.ScheduleClubModel
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
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
    override fun onBindViewHolder(holder: FindClubHolder, position: Int) {

        val model: ScheduleClubModel = items[position]

        holder.timeStart.text = model.startTime

        holder.timeEnd.text = model.endTime

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

        if (!TextUtils.isEmpty(model.photoUrl)) {
            context?.let {
                Glide.with(it).asBitmap().load(model.photoUrl).placeholder(R.drawable.ic_no_image_grey)
                    .into(holder.imageView)
            }
        } else {
            holder.imageView.setImageResource(R.drawable.ic_no_image_grey)
        }

        holder.tvName.text = model.nameClub

        holder.tvPhone.text =
            if (isLoggedUser) model.phone else context?.getString(R.string.need_login_to_see)

        holder.call.setOnClickListener {
            itemClickInterface.onItemClick(model, position, Enum.EnumTypeClick.Phone.value)
        }

        holder.containerView.setOnClickListener {
            itemClickInterface.onItemClick(model, position, Enum.EnumTypeClick.View.value)

        }
    }
}

