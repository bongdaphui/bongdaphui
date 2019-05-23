package com.bongdaphui.approvePlayer

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
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mikhaellopez.circularimageview.CircularImageView

/**
 * Created by ChuTien on ${1/25/2017}.
 */
class ApproveAdapterPlayer(
    var ctx: Context,
    val items: ArrayList<PlayerApprove>,
    private var itemClickInterface: OnItemClickListener<PlayerApprove>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_ITEM = 1
    private val VIEW_SECTION = 0

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val player = items[position]
        if (holder is OriginalViewHolder) {
            holder.name.setText(player.name)
            holder.message.setText(player.message)
            if (!TextUtils.isEmpty(player.photoUrl)) {
                Glide.with(ctx).load(
                    player.photoUrl
                )
                    .into(holder.image)
            }

            holder.itemView.setOnClickListener {
                itemClickInterface.onItemClick(player, position, Enum.EnumTypeClick.View.value)
            }


        } else {
            val view = holder as SectionViewHolder
            view.title_section.setText(player.name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType == VIEW_ITEM) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_player_approve, parent, false)
            vh = OriginalViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_section, parent, false)
            vh = SectionViewHolder(v)
        }
        return vh

    }

    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var image: ImageView
        var name: TextView
        var message: TextView

        init {
            image = v.findViewById(R.id.image) as CircularImageView
            name = v.findViewById(R.id.name) as TextView
            message = v.findViewById(R.id.message) as TextView

        }
    }

    class SectionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var title_section: TextView

        init {
            title_section = v.findViewById(R.id.title_section) as TextView
        }
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (this.items[position].section) VIEW_SECTION else VIEW_ITEM
    }

    fun insertItem(index: Int, player: PlayerApprove) {
        items.add(index, player)
        notifyItemInserted(index)
    }
}

