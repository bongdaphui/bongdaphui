package com.bongdaphui.comment

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bongdaphui.R
import com.mikhaellopez.circularimageview.CircularImageView

class CommentHolder(row: View) : RecyclerView.ViewHolder(row) {

    var ivUser: CircularImageView = row.findViewById(R.id.item_comment_iv_user)

    var tvName: TextView = row.findViewById(R.id.item_comment_tv_name)

    var tvContent: TextView = row.findViewById(R.id.item_comment_tv_content)

    var tvTime: TextView = row.findViewById(R.id.item_comment_tv_time)

}
