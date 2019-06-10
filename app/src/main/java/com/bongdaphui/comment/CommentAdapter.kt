package com.bongdaphui.comment

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.CommentModel
import com.bongdaphui.utils.DateTimeAgo
import com.bongdaphui.utils.DateTimeUtil
import com.bumptech.glide.Glide
import java.util.*

class CommentAdapter(
    var context: Context?,
    private val items: ArrayList<CommentModel>,
    private var itemClickInterface: OnItemClickListener<CommentModel>
) : RecyclerView.Adapter<CommentHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CommentHolder {
        return CommentHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, p0, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {

        val model: CommentModel = items[position]

        if (!TextUtils.isEmpty(model.photoUser)) {
            context?.let {
                Glide.with(it).asBitmap().load(model.photoUser).placeholder(R.drawable.ic_person_grey)
                    .into(holder.ivUser)
            }
        } else {
            holder.ivUser.setImageResource(R.drawable.ic_person_grey)
        }

        holder.tvName.text = model.nameUser

        holder.tvContent.text = model.content

        val text = model.time?.let { it ->
            DateTimeUtil().getTimeInMilliseconds(it, DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format)?.let {
                context?.let { it1 -> DateTimeAgo().timeAgo(it1, it) }
            }
        }
        holder.tvTime.text = text
    }
}

