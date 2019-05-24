package com.bongdaphui.field

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide

class FieldAdapter(
    var context: Context?,
    private val items: ArrayList<FbFieldModel>,
    private val isLoggedUser: Boolean = true,
    private var itemClickInterface: OnItemClickListener<FbFieldModel>
) : RecyclerView.Adapter<FieldHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FieldHolder {

        return FieldHolder(LayoutInflater.from(context).inflate(R.layout.item_field, p0, false))

    }

    override fun getItemCount(): Int {

        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: FieldHolder, position: Int) {

        val model: FbFieldModel = items[position]

        if (!TextUtils.isEmpty(model.photoUrl)) {
            context?.let { Glide.with(it).asBitmap().load(model.photoUrl).into(viewHolder.image) }
        }else{
            viewHolder.image.setImageResource(R.drawable.ic_no_image_grey)
        }

        viewHolder.name.text = "Sân bóng đá ${model.name}"

        viewHolder.phone.text =
            if (isLoggedUser) model.phone else context!!.getString(R.string.need_login_to_see)

        viewHolder.call.setOnClickListener {
            itemClickInterface.onItemClick(model, position, Enum.EnumTypeClick.Phone.value)

        }

        viewHolder.address.text = model.address

        if (Utils().isEmpty(model.amountField)) {

            viewHolder.amountField.text = context!!.resources.getString(R.string.not_yet_update)

        } else {

            viewHolder.amountField.text = model.amountField
        }

        if (Utils().isEmpty(model.price)) {

            viewHolder.price.text = context!!.resources.getString(R.string.not_yet_update)

        } else {

            viewHolder.price.text =
                "${Utils().formatMoney(Constant().ONE_DECIMAL_FORMAT, model.price!!)} VND"
        }

        viewHolder.container.setOnClickListener {
            itemClickInterface.onItemClick(model, position, Enum.EnumTypeClick.View.value)
        }

    }

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }
}

