package com.bongdaphui.club

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.ClubModel
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide

class ClubAdapter(
    var context: Context?,
    private val items: ArrayList<ClubModel>,
    private val isLoggedUser: Boolean = true,
    var onItemClickListener: OnItemClickListener<ClubModel>
) : RecyclerView.Adapter<ClubHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ClubHolder {

        return ClubHolder(LayoutInflater.from(context).inflate(R.layout.item_club, p0, false))

    }

    override fun getItemCount(): Int {

        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ClubHolder, position: Int) {

        val model: ClubModel = items[position]

        if (!TextUtils.isEmpty(model.photo)) {
            context?.let { Glide.with(it).asBitmap().load(model.photo).into(viewHolder.photo) }
        } else {
            viewHolder.photo.setImageResource(R.drawable.ic_no_image_grey)
        }

        viewHolder.nameFC.text = model.name

        viewHolder.nameCaption.text =
            if (model.caption.isEmpty()) context?.resources?.getText(R.string.not_update) else model.caption

        val address = if (model.address.isNotEmpty()) "${model.address}, " else ""

        viewHolder.area.text =
            "$address${context?.let {
                model.idCity.let { it1 ->
                    Utils().getNameCityDistrictFromId(
                        it,
                        it1, model.idDistrict
                    )
                }
            }}"

        viewHolder.phone.text =
            if (isLoggedUser) model.phone else context!!.getString(R.string.need_login_to_see)

        viewHolder.call.setOnClickListener {
            onItemClickListener.onItemClick(model, position, Enum.EnumTypeClick.Phone.value)
        }

        viewHolder.amountPlayer.text = "${model.getAmountPlayer()}"

        viewHolder.container.setOnClickListener {
            onItemClickListener.onItemClick(model, position, Enum.EnumTypeClick.View.value)
        }

    }

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }
}

