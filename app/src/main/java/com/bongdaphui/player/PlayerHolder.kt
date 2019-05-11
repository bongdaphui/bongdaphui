package com.bongdaphui.player

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bongdaphui.R

class PlayerHolder(row: View) : RecyclerView.ViewHolder(row) {

    var photo: ImageView = row.findViewById(R.id.item_member_image)

    var name: TextView = row.findViewById(R.id.item_member_name)

//        var email: TextView = row.findViewById(R.id.item_member_email)

//        var phone: TextView = row.findViewById(R.id.item_member_phone)

    var dob: TextView = row.findViewById(R.id.item_member_dob)

//        var address: TextView = row.findViewById(R.id.item_member_address)

    var height: TextView = row.findViewById(R.id.item_member_height)

    var weight: TextView = row.findViewById(R.id.item_member_weight)

    var positon: TextView = row.findViewById(R.id.item_member_position)

    var clotherNumber: TextView = row.findViewById(R.id.item_member_clother_number)
}
