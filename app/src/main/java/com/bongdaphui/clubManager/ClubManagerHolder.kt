package com.bongdaphui.clubManager

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bongdaphui.R

class ClubManagerHolder(row: View) : RecyclerView.ViewHolder(row) {

    var container: View = row.findViewById(R.id.item_manager_club_container)

    var photo: ImageView = row.findViewById(R.id.item_manager_club_image)

    var name: TextView = row.findViewById(R.id.item_manager_club_name)

    var amountPlayer: TextView = row.findViewById(R.id.item_manager_club_amount)

}
