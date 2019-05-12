package com.bongdaphui.footballField

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bongdaphui.R
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

class AdapterSectionedField : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_ITEM = 1
    private val VIEW_SECTION = 0

    private var items = ArrayList<FbFieldModel>()
    private lateinit var ctx: Context
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: FbFieldModel, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    fun AdapterSectionedField(context: Context, items: ArrayList<FbFieldModel>) {
        this.items = items
        ctx = context
    }

    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var container: View = v.findViewById(R.id.item_football_field_container)

        var image: ImageView = v.findViewById(R.id.item_football_field_image)

        var name: TextView = v.findViewById(R.id.item_football_field_name)

        var phone: TextView = v.findViewById(R.id.item_football_field_phone)

        var address: TextView = v.findViewById(R.id.item_football_field_address)

        var amountField: TextView = v.findViewById(R.id.item_football_field_amount)

        var price: TextView = v.findViewById(R.id.item_football_field_price)
    }

    class SectionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var titleSection: TextView = v.findViewById(R.id.title_section) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType == VIEW_ITEM) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_field, parent, false)
            vh = OriginalViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_section, parent, false)
            vh = SectionViewHolder(v)
        }
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val field = items[position]
        if (holder is OriginalViewHolder) {

            /*holder.name.text = p.name
            holder.lyt_parent.setOnClickListener(View.OnClickListener { view ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, items[position], position)
                }
            })*/


            Glide.with(ctx).load(
                if (Utils().isEmpty(field.photoUrl))
                    Utils().getDrawable(ctx!!, R.drawable.ic_picture) else field.photoUrl
            )
                .apply(RequestOptions.circleCropTransform())
                .into(holder.image)

            holder.name.text = "Sân bóng đá ${field.name}"

            if (Utils().isEmpty(field.phone)) {

                holder.phone.text = ctx!!.resources.getString(R.string.not_yet_update)

            } else {

                holder.phone.text = Html.fromHtml("<u>${field.phone}</u>")

                /*holder.phone.setOnClickListener {
                    itemClickInterface.onItemClick(field, position, Enum.EnumTypeClick.Phone.value)
                }*/

            }

            holder.address.text = field.address

            if (Utils().isEmpty(field.amountField)) {

                holder.amountField.text = ctx.resources.getString(R.string.not_yet_update)

            } else {

                holder.amountField.text = field.amountField
            }

            if (Utils().isEmpty(field.price)) {

                holder.price.text = ctx.resources.getString(R.string.not_yet_update)

            } else {

                holder.price.text =
                    "${Utils().formatMoney(Constant().ONE_DECIMAL_FORMAT, field.price!!)} VND"
            }

            /*holder.container.setOnClickListener {
                itemClickInterface.onItemClick(field, position, Enum.EnumTypeClick.View.value)
            }*/

        } else {
            val view = holder as SectionViewHolder
            view.titleSection.text = field.name
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (this.items[position].section) VIEW_SECTION else VIEW_ITEM
    }

    fun insertItem(index: Int, fbFieldModel: FbFieldModel) {
        items.add(index, fbFieldModel)
        notifyItemInserted(index)
    }

}