package com.bongdaphui.findPlayer

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bongdaphui.R

class FindPlayerHolder(row: View) : RecyclerView.ViewHolder(row) {


    var containerView: CardView = row.findViewById(R.id.item_find_player_container)

    var timeStart: TextView = row.findViewById(R.id.item_find_player_time_start)

    var timeEnd: TextView = row.findViewById(R.id.item_find_player_time_end)

    var imageView: ImageView = row.findViewById(R.id.item_find_player_iv_user)

    var tvName: TextView = row.findViewById(R.id.item_find_player_tv_name)

    var tvPhone: TextView = row.findViewById(R.id.item_find_player_tv_phone)

}
