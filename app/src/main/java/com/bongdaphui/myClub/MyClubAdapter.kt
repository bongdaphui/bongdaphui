package com.bongdaphui.myClub

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.ClubModel
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide

class MyClubAdapter(
    var context: Context?,
    private val items: ArrayList<ClubModel>,
    private val uid: String,
    private var onItemClickListener: OnItemClickListener<ClubModel>
) : RecyclerView.Adapter<MyClubHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyClubHolder {

        return MyClubHolder(LayoutInflater.from(context).inflate(R.layout.item_club, p0, false))

    }

    override fun getItemCount(): Int {

        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: MyClubHolder, position: Int) {

        val club: ClubModel = items[position]

        if (club.photo.isNotEmpty()) {
            Glide.with(context!!).asBitmap().load(club.photo)
                .into(viewHolder.photo)
        }

        viewHolder.nameFC.text = club.name

        viewHolder.nameCaption.text =
            if (club.caption.isEmpty()) context!!.resources.getText(R.string.not_update) else club.caption

        val address = if (club.address.isNotEmpty()) "${club.address}, " else ""

        viewHolder.area.text =
            "$address${context?.let {
                club.idCity.let { it1 ->
                    Utils().getNameCityDistrictFromId(
                        it,
                        it1, club.idDistrict
                    )
                }
            }}"

        viewHolder.phone.text = club.phone

        viewHolder.call.setOnClickListener {
            onItemClickListener.onItemClick(club, position, Enum.EnumTypeClick.Phone.value)
        }

        viewHolder.amountPlayer.text = "${club.getAmountPlayer()}"

        viewHolder.addSchedule.visibility = if (uid == club.idCaptain) View.VISIBLE else View.GONE

        viewHolder.addSchedule.text = Html.fromHtml("<u>${context!!.resources.getText(R.string.add_schedule_club)}</u>")
        viewHolder.addSchedule.setOnClickListener {

            onItemClickListener.onItemClick(club, position, Enum.EnumTypeClick.AddSchedule.value)
        }

        viewHolder.container.setOnClickListener {
            onItemClickListener.onItemClick(club, position, Enum.EnumTypeClick.View.value)
        }

    }

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }
}

