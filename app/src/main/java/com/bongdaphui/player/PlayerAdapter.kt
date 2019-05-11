package com.bongdaphui.player

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.model.PlayerModel
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PlayerAdapter(var context: Context?, val items: ArrayList<PlayerModel>) :

    RecyclerView.Adapter<PlayerHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PlayerHolder {

        return PlayerHolder(LayoutInflater.from(context).inflate(R.layout.item_player, p0, false))

    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: PlayerHolder, position: Int) {

        val player: PlayerModel = items[position]


        Glide.with(context!!).load(
            if (Utils().isEmpty(player.photo))
                Utils().getDrawable(context!!, R.drawable.ic_personal) else player.photo
        )
            .apply(RequestOptions.circleCropTransform())
            .into(viewHolder.photo)

        viewHolder.name.text = player.name

//        viewHolder.email.text = Html.fromHtml("<b>Email: </b> ${player.email}")

//        viewHolder.phone.text = Html.fromHtml("<b>SĐT: </b> ${player.phone}")

        viewHolder.dob.text = "${player.dob}"

//        viewHolder.address.text = Html.fromHtml("<b>Địa chỉ: </b> ${player.address}")

        viewHolder.height.text = "${player.height} cm"

        viewHolder.weight.text = "${player.weight} kg"

        viewHolder.positon.text = player.position

        viewHolder.clotherNumber.text = player.clothesNumber
    }

}

