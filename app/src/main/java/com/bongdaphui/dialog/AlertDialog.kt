package com.bongdaphui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.bongdaphui.R
import com.bongdaphui.listener.AcceptListener

class AlertDialog {

    fun showCustomDialog(
        context: Context,
        title: String,
        content: String,
        decline: String,
        accept: String,
        acceptListener: AcceptListener
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_custom)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT


        (dialog.findViewById(R.id.tv_title) as TextView).text = title

        (dialog.findViewById(R.id.tv_content) as TextView).text = content

        (dialog.findViewById(R.id.bt_decline) as TextView).text = decline

        (dialog.findViewById(R.id.bt_accept) as TextView).text = accept

        (dialog.findViewById(R.id.bt_decline) as Button).setOnClickListener {
            dialog.dismiss()
        }

        (dialog.findViewById(R.id.bt_accept) as Button).setOnClickListener {
            acceptListener.onAccept()
            dialog.dismiss()
        }

        dialog.show()
        dialog.window!!.attributes = lp
    }
}