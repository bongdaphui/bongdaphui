package com.bongdaphui.dialog

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bongdaphui.R
import com.bongdaphui.model.TutorialModel
import com.bumptech.glide.Glide

class TutorialAdapter(val context: Context, val inflater: LayoutInflater, val items: ArrayList<TutorialModel>) : PagerAdapter() {

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val holder = inflater.inflate(R.layout.item_tutorial_step, container, false)

        container.addView(holder, 0)

        val welcomeModel = items[position]


        if (null != holder) {

            val imageView = holder.findViewById(R.id.item_welcome_step_image) as ImageView
            Glide.with(context).asBitmap().load(welcomeModel.photo).into(imageView)

            val title = holder.findViewById(R.id.item_welcome_step_title) as TextView
            title.text = welcomeModel.title


            val content = holder.findViewById(R.id._item_welcome_step_content) as TextView
            content.text = welcomeModel.content

        }

        return holder
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {

        return p0 == p1
    }

    override fun getCount(): Int {

        return items.size
    }

}