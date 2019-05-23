package com.bongdaphui.approvePlayer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide
import com.mikhaellopez.circularimageview.CircularImageView

/**
 * Created by ChuTien on ${1/25/2017}.
 */
class ApproveAdapterPlayer(
    var ctx: Context,
    val items: ArrayList<PlayerApprove>,
    private var itemClickInterface: OnItemClickListener<PlayerApprove>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val viewItem = 1
    private val viewSection = 0

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val player = items[position]
        if (holder is OriginalViewHolder) {
            holder.name.text = player.name
            holder.message.text = player.message

            if (!Utils().isEmpty(player.photoUrl))
                Glide.with(ctx).load(player.photoUrl).into(holder.image)

            holder.itemView.setOnClickListener {
                itemClickInterface.onItemClick(player, position, Enum.EnumTypeClick.View.value)
            }


        } else {
            val view = holder as SectionViewHolder
            view.titleSection.text = player.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType == viewItem) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_player_approve, parent, false)
            vh = OriginalViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_section, parent, false)
            vh = SectionViewHolder(v)
        }
        return vh

    }

    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var image: ImageView = v.findViewById(R.id.image) as CircularImageView
        var name: TextView = v.findViewById(R.id.name) as TextView
        var message: TextView = v.findViewById(R.id.message) as TextView

    }

    class SectionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var titleSection: TextView = v.findViewById(R.id.title_section) as TextView

    }

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (this.items[position].section) viewSection else viewItem
    }

    fun insertItem(index: Int, player: PlayerApprove) {
        items.add(index, player)
        notifyItemInserted(index)
    }
}

