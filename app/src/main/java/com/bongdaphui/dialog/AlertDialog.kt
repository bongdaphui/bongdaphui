package com.bongdaphui.dialog

import android.content.Context
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.bongdaphui.R
import com.bongdaphui.listener.ConfirmListener
import com.bongdaphui.utils.Enum

class AlertDialog {

    fun showDialog(context: Context, id: Int, listener: ConfirmListener, msg:String ="") {
        var message = msg
        if (TextUtils.isEmpty(message)) {
            when (id) {
                Enum.EnumConfirmYes.Logout.value -> message = context.resources.getString(R.string.are_you_want_logout)
                Enum.EnumConfirmYes.DeniedPermission.value -> message = context.resources.getString(R.string.alert_denied_permission)
                Enum.EnumConfirmYes.RequestJoinClubFail.value -> message = context.resources.getString(R.string.request_join_club_fail)
                Enum.EnumConfirmYes.RequestJoinClubSuccess.value -> message = context.resources.getString(R.string.request_join_club_success)
                Enum.EnumConfirmYes.FeatureNeedLogin.value -> message = context.resources.getString(R.string.this_feature_need_login)
                Enum.EnumConfirmYes.FieldIsAvailable.value -> message = context.resources.getString(R.string.add_field_is_available)
                Enum.EnumConfirmYes.AddFieldFail.value -> message = context.resources.getString(R.string.add_field_fail)
                Enum.EnumConfirmYes.AddFieldSuccess.value -> message = context.resources.getString(R.string.add_field_success)
                Enum.EnumConfirmYes.UpdateSuccess.value -> message = context.resources.getString(R.string.update_success)
                Enum.EnumConfirmYes.UpdateFail.value -> message = context.resources.getString(R.string.update_fail)
                Enum.EnumConfirmYes.ValidDateSchedule.value -> message = context.resources.getString(R.string.valid_input_date_schedule)
                Enum.EnumConfirmYes.AddScheduleSuccess.value -> message = context.resources.getString(R.string.add_schedule_player_success)
                Enum.EnumConfirmYes.AddScheduleFail.value -> message = context.resources.getString(R.string.add_schedule_player_fail)
            }

        }

        val alertDialog = AlertDialog.Builder(context)

            //set icon
            .setIcon(android.R.drawable.ic_dialog_alert)

            //set title
            .setTitle(context.resources.getString(R.string.alert))

            //set message
            .setMessage(message)

            //set positive button
            .setPositiveButton(
                if (id == Enum.EnumConfirmYes.Logout.value || id == Enum.EnumConfirmYes.FeatureNeedLogin.value)
                    context.resources.getString(R.string.yes)
                else context.resources.getString(R.string.close)
            ) { dialog, _ ->

                listener.onConfirm(id)

                dialog.dismiss()
            }

            //set negative button
            .setNegativeButton(
                if (id == Enum.EnumConfirmYes.Logout.value || id == Enum.EnumConfirmYes.FeatureNeedLogin.value) context.resources.getString(R.string.no)
                else ""
            ) { dialog, _ ->

                dialog.dismiss()
            }


        alertDialog.show()
    }

}