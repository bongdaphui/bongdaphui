package com.bongdaphui.comment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.CommentModel
import com.bongdaphui.utils.DateTimeUtil
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import java.util.*

class CommentAdapter(
    var context: Context?,
    private val items: ArrayList<CommentModel>,
    private var itemClickInterface: OnItemClickListener<CommentModel>
) : RecyclerView.Adapter<CommentHolder>() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val localeByLanguageTag = Locale.forLanguageTag("vn")!!
    private val messages = TimeAgoMessages.Builder().withLocale(localeByLanguageTag).build()

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
                TimeAgo.using(it, messages)
            }
        }
        holder.tvTime.text = text
    }
}

