package com.bongdaphui.myClub

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bongdaphui.R

class MyClubHolder(row: View) : RecyclerView.ViewHolder(row) {

    var container: View = row.findViewById(R.id.item_club_container)

    var photo: ImageView = row.findViewById(R.id.item_club_image)

    var nameFC: TextView = row.findViewById(R.id.item_club_name)

    var nameCaption: TextView = row.findViewById(R.id.item_club_caption)

    var area: TextView = row.findViewById(R.id.item_club_area)

    var phone: TextView = row.findViewById(R.id.item_club_phone)

    var amountPlayer: TextView = row.findViewById(R.id.item_club_amount_player)

}
