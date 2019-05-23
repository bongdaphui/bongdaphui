package com.bongdaphui.player

import android.content.Context
import android.support.v7.widget.RecyclerView
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
import com.bumptech.glide.request.RequestOptions

/**
 * Created by ChuTien on ${1/25/2017}.
 */
class PlayerStickAdapter(var ctx: Context, val items: ArrayList<UserStickModel>, private var itemClickInterface: OnItemClickListener<UserStickModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userModule = items[position]
        if (holder is OriginalViewHolder) {
            holder.name.setText(userModule.name)
            holder.position.setText(userModule.position)
            Glide.with(ctx).load(
                if (Utils().isEmpty(userModule.photoUrl))
                    Utils().getDrawable(ctx, R.drawable.ic_personal) else userModule.photoUrl
            )
                .apply(RequestOptions.circleCropTransform())
                .into(holder.image)

            holder.itemView.setOnClickListener {
                itemClickInterface.onItemClick(userModule,position,Enum.EnumTypeClick.View.value)
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
        var image: ImageView
        var name: TextView
        var position: TextView

        init {
            image = v.findViewById(R.id.player_photo) as ImageView
            name = v.findViewById(R.id.player_name) as TextView
            position = v.findViewById(R.id.player_position) as TextView

        }
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }
}