package com.bongdaphui.player

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.UserStickModel
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide

/**
 * Created by ChuTien on ${1/25/2017}.
 */
class PlayerStickAdapter(
    private var ctx: Context,
    private val items: ArrayList<UserStickModel>,
    private var itemClickInterface: OnItemClickListener<UserStickModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userModule = items[position]
        if (holder is OriginalViewHolder) {

            holder.name.text =
                if (TextUtils.isEmpty(userModule.name)) ctx.resources.getString(R.string.not_update) else userModule.name

            holder.position.text =
                if (TextUtils.isEmpty(userModule.position)) ctx.resources.getString(R.string.not_update) else Utils().getPosition(
                    userModule.position.toInt()
                )

            if (userModule.photoUrl.isNotEmpty())
                Glide.with(ctx).asBitmap().load(userModule.photoUrl).placeholder(R.drawable.ic_person_grey).into(holder.image)

            holder.itemView.setOnClickListener {
                itemClickInterface.onItemClick(userModule, position, Enum.EnumTypeClick.View.value)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_player_stick, parent, false)
        vh = OriginalViewHolder(v)
        return vh
    }

    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var image: ImageView = v.findViewById(R.id.player_photo) as ImageView
        var name = v.findViewById(R.id.player_name) as TextView
        var position: TextView = v.findViewById(R.id.player_position) as TextView
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }
}