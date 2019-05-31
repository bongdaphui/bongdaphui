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
            context?.let {
                Glide.with(it).asBitmap().load(model.photoUrl).placeholder(R.drawable.bg_field).into(viewHolder.image)
            }
        } else {
            viewHolder.image.setImageResource(R.drawable.bg_field)
        }

        viewHolder.name.text = model.name

        val phone = if (TextUtils.isEmpty(model.phone2)) model.phone else "${model.phone} - ${model.phone2}"

        viewHolder.phone.text =
            if (isLoggedUser) phone else context!!.getString(R.string.need_login_to_see)

        viewHolder.call.setOnClickListener {
            itemClickInterface.onItemClick(model, position, Enum.EnumTypeClick.Phone.value)

        }

        viewHolder.address.text = model.address

        if (Utils().isEmpty(model.amountField)) {

            viewHolder.amountField.text = context?.resources?.getString(R.string.three_dot)

        } else {

            viewHolder.amountField.text = model.amountField
        }

        if (TextUtils.isEmpty(model.price) && TextUtils.isEmpty(model.priceMax)) {

            viewHolder.price.text = context?.resources?.getString(R.string.three_dot)

        } else {

            val min =
                if (TextUtils.isEmpty(model.price)) context?.resources?.getString(R.string.three_dot) else
                    Utils().formatMoney(Constant().oneDecimalFormat, model.price!!)

            val max =
                if (TextUtils.isEmpty(model.priceMax)) context?.resources?.getString(R.string.three_dot) else
                    Utils().formatMoney(Constant().oneDecimalFormat, model.priceMax!!)

            viewHolder.price.text = "$min - $max"
        }

        viewHolder.container.setOnClickListener {
            itemClickInterface.onItemClick(model, position, Enum.EnumTypeClick.View.value)
        }
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }
}

