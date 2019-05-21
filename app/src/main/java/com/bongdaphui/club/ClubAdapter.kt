package com.bongdaphui.club

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.ClubModel
import com.bongdaphui.utils.Enum
import com.bumptech.glide.Glide

class ClubAdapter(
    var context: Context?,
    private val items: ArrayList<ClubModel>,
    var onItemClickListener: OnItemClickListener<ClubModel>
) : RecyclerView.Adapter<ClubHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ClubHolder {

        return ClubHolder(LayoutInflater.from(context).inflate(R.layout.item_club, p0, false))

    }

    override fun getItemCount(): Int {

        return items.size
    }

    override fun onBindViewHolder(viewHolder: ClubHolder, position: Int) {

        val club: ClubModel = items[position]

        if (club.photo?.isNotEmpty()!!) {
            Glide.with(context!!).asBitmap().load(club.photo).into(viewHolder.photo)
        }

        viewHolder.nameFC.text = club.name

        if (club.caption?.isEmpty()!!) {
            viewHolder.nameCaption.text =
                Html.fromHtml("Đội trưởng: <b>${context?.resources?.getText(R.string.not_update)}</b>")

        } else {
            viewHolder.nameCaption.text = Html.fromHtml("Đội trưởng: <b>${club.caption}</b>")
        }

        viewHolder.area.text = Html.fromHtml("Địa chỉ: <b>${club.address}</b>")

        viewHolder.phone.text = Html.fromHtml("<u>${club.phone}</u>")

        viewHolder.phone.setOnClickListener {
            onItemClickListener.onItemClick(club, position, Enum.EnumTypeClick.Phone.value)
        }

        viewHolder.amountPlayer.text = "${club.getAmountPlayer()}"

        viewHolder.container.setOnClickListener {
            onItemClickListener.onItemClick(club, position, Enum.EnumTypeClick.View.value)
        }

    }

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }
}

