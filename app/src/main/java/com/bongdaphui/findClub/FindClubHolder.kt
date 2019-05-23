package com.bongdaphui.findClub

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bongdaphui.R

class FindClubHolder(row: View) : RecyclerView.ViewHolder(row) {


    var containerView: CardView = row.findViewById(R.id.item_find_container)

    var timeStart: TextView = row.findViewById(R.id.item_find_time_start)

    var timeEnd: TextView = row.findViewById(R.id.item_find_time_end)

    var imageView: ImageView = row.findViewById(R.id.item_find_iv_user)

    var tvName: TextView = row.findViewById(R.id.item_find_tv_name)

    var tvPhone: TextView = row.findViewById(R.id.item_find_tv_phone)

    var typeField: TextView = row.findViewById(R.id.item_find_type_field)

    var call: ImageButton = row.findViewById(R.id.item_find_ib_call)

}
