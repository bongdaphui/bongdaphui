package com.bongdaphui.footballField

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
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

        val fbFieldModel: FbFieldModel = items[position]

        if (fbFieldModel.photoUrl?.isNotEmpty()!!) {
            Glide.with(this.context!!).asBitmap().load(fbFieldModel.photoUrl)
                .placeholder(context?.resources?.getDrawable(R.drawable.ic_person_grey))
                .into(viewHolder.image)
        }

        viewHolder.name.text = "Sân bóng đá ${fbFieldModel.name}"

        viewHolder.phone.text =
            if (isLoggedUser) fbFieldModel.phone else context!!.getString(R.string.need_login_to_see)

        viewHolder.call.setOnClickListener {
            itemClickInterface.onItemClick(fbFieldModel, position, Enum.EnumTypeClick.Phone.value)

        }

        viewHolder.address.text = fbFieldModel.address

        if (Utils().isEmpty(fbFieldModel.amountField)) {

            viewHolder.amountField.text = context!!.resources.getString(R.string.not_yet_update)

        } else {

            viewHolder.amountField.text = fbFieldModel.amountField
        }

        if (Utils().isEmpty(fbFieldModel.price)) {

            viewHolder.price.text = context!!.resources.getString(R.string.not_yet_update)

        } else {

            viewHolder.price.text =
                "${Utils().formatMoney(Constant().ONE_DECIMAL_FORMAT, fbFieldModel.price!!)} VND"
        }

        viewHolder.container.setOnClickListener {
            itemClickInterface.onItemClick(fbFieldModel, position, Enum.EnumTypeClick.View.value)
        }

    }

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }
}

