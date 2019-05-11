package com.bongdaphui.addField

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bongdaphui.R

class SpinnerAdapter(context: Context, resource: Int, listCities: ArrayList<String>) :
    ArrayAdapter<String>(context, resource, listCities) {
    private var list: ArrayList<String> = listCities

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return spinnerView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return spinnerView(position, parent)
    }

    private fun spinnerView(position: Int, parent: ViewGroup): View {

        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val customView = layoutInflater.inflate(R.layout.item_spinner, parent, false)

        val textView = customView.findViewById(R.id.item_spinner_tv_name) as TextView

        textView.text = list[position]

        return customView
    }
}