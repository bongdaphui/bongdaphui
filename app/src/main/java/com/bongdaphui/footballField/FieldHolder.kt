package com.bongdaphui.footballField

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bongdaphui.R

class FieldHolder(row: View) : RecyclerView.ViewHolder(row) {

    var container: View = row.findViewById(R.id.item_field_container)

    var image: ImageView = row.findViewById(R.id.item_field_image)

    var name: TextView = row.findViewById(R.id.item_football_field_name)

    var phone: TextView = row.findViewById(R.id.item_field_phone)

    var address: TextView = row.findViewById(R.id.item_field_address)

    var amountField: TextView = row.findViewById(R.id.item_field_amount)

    var price: TextView = row.findViewById(R.id.item_field_price)

    var call: ImageButton = row.findViewById(R.id.item_field_ib_call)

}
