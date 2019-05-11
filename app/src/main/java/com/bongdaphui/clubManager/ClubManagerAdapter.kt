package com.bongdaphui.clubManager

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.ItemClickInterface
import com.bongdaphui.model.ClubModel
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ClubManagerAdapter(
    var context: Context?,
    private val items: ArrayList<ClubModel>,
    var itemClickInterface: ItemClickInterface<ClubModel>
) : RecyclerView.Adapter<ClubManagerHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ClubManagerHolder {

        return ClubManagerHolder(LayoutInflater.from(context).inflate(R.layout.item_manager_club, p0, false))

    }

    override fun getItemCount(): Int {

        return items.size
    }

    override fun onBindViewHolder(viewHolder: ClubManagerHolder, position: Int) {

        val club: ClubModel = items[position]

        Glide.with(context!!).load(
            if (club.photo!!.isEmpty()) Utils().getDrawable(context!!, R.drawable.ic_picture) else club.photo
        )
            .apply(RequestOptions.circleCropTransform())
            .into(viewHolder.photo)

        viewHolder.name.text = club.name

        viewHolder.amountPlayer.text = Html.fromHtml("Số thành viên: <b>${club.getAmountPlayer()}</b>")

        viewHolder.container.setOnClickListener {
            itemClickInterface.OncItemlick(club, position, Enum.EnumTypeClick.View.value)
        }

    }
}

