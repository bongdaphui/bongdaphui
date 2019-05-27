package com.bongdaphui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.bongdaphui.R
import com.bongdaphui.model.TutorialModel
import com.bongdaphui.utils.SharedPreference

class TutorialDialog {
    fun show(context: Context, list: ArrayList<TutorialModel>) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_welcome)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT

        val adapter = TutorialAdapter(context, LayoutInflater.from(context), list)

        val viewPager: ViewPager = dialog.findViewById(R.id.dialog_welcome_view_pager) as ViewPager

        viewPager.adapter = adapter

        bottomProgressDots(context, dialog, 0, list)

        val btnNext: Button = dialog.findViewById(R.id.dialog_welcome_btn_next) as Button

        //  viewpager change listener
        val viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
            object : ViewPager.OnPageChangeListener {

                override fun onPageSelected(position: Int) {
                    bottomProgressDots(context, dialog, position, list)

                    if (position == list.size - 1) {
                        context.let { SharedPreference(it).save(SharedPreference.KeyName.KEY_TUTORIAL.name, true) }
                        btnNext.text = context.getString(R.string.close)
                        btnNext.setBackgroundColor(context.resources.getColor(R.color.orange_400))
                        btnNext.setTextColor(Color.WHITE)

                    } else {
                        btnNext.text = context.getString(R.string.next)
                        btnNext.setBackgroundColor(context.resources.getColor(R.color.grey_10))
                        btnNext.setTextColor(context.resources.getColor(R.color.grey_90))
                    }
                }

                override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

                }

                override fun onPageScrollStateChanged(arg0: Int) {

                }
            }

        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        btnNext.setOnClickListener {
            val current = viewPager.currentItem + 1
            if (current < list.size) {
                // move to next screen
                viewPager.currentItem = current
            } else {
                dialog.dismiss()
            }
        }

        dialog.show()
        dialog.window!!.attributes = lp

    }

    fun bottomProgressDots(context: Context, dialog: Dialog, currentIndex: Int, list: ArrayList<TutorialModel>) {
        val dotsLayout = dialog.findViewById(R.id.dialog_welcome_layout_dots) as LinearLayout
        val dots = arrayOfNulls<ImageView>(list.size)

        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(context)
            val widthHeight = 15
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(widthHeight, widthHeight))
            params.setMargins(10, 10, 10, 10)
            dots[i]?.layoutParams = params
            dots[i]?.setImageResource(R.drawable.shape_circle)
            dots[i]?.setColorFilter(context.resources.getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN)
            dotsLayout.addView(dots[i])
        }

        if (dots.isNotEmpty()) {
            dots[currentIndex]?.setImageResource(R.drawable.shape_circle)
            dots[currentIndex]?.setColorFilter(context.resources.getColor(R.color.orange_400), PorterDuff.Mode.SRC_IN)
        }
    }
}