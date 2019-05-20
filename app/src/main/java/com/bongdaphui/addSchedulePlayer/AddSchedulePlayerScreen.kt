package com.bongdaphui.addSchedulePlayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.AddScheduleListener
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.listener.ConfirmListener
import com.bongdaphui.model.SchedulePlayerModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.DateTimeUtil
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.frg_add_schedule_player.*
import java.util.*


class AddSchedulePlayerScreen : BaseFragment() {

    private var idCity: String = ""
    private var idDistrict: String = ""

    companion object {

        private var addScheduleListener: AddScheduleListener? = null

        fun getInstance(listener: AddScheduleListener): AddSchedulePlayerScreen {

            addScheduleListener = listener

            return AddSchedulePlayerScreen()

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_add_schedule_player, container, false)

    }

    override fun onResume() {

        super.onResume()

        showButtonBack(true)

        showFooter(false)

        setTitle(activity!!.resources.getString(R.string.add_schedule_of_you))
    }

    override fun onBindView() {

        initView()

        initSpinner()

        onClick()
    }

    private fun initView() {

        val current = Calendar.getInstance().timeInMillis

        frg_add_schedule_player_bt_from_date.text =
            DateTimeUtil().getFormat(current, DateTimeUtil.DateFormatDefinition.DD_MM_YYYY.format)

        frg_add_schedule_player_bt_from_time.text =
            DateTimeUtil().getFormat(current, DateTimeUtil.DateFormatDefinition.HH_MM.format)

        val currentPlus = Calendar.getInstance().timeInMillis + 86400000// plus 1 day

        frg_add_schedule_player_bt_to_date.text =
            DateTimeUtil().getFormat(currentPlus, DateTimeUtil.DateFormatDefinition.DD_MM_YYYY.format)

        frg_add_schedule_player_bt_to_time.text =
            DateTimeUtil().getFormat(currentPlus, DateTimeUtil.DateFormatDefinition.HH_MM.format)

    }

    private fun initSpinner() {
        Utils().initSpinnerCity(
            activity!!,
            frg_add_schedule_player_sp_city,
            frg_add_schedule_player_sp_district,
            object :
                BaseSpinnerSelectInterface {
                override fun onSelectCity(_idCity: String, _idDistrict: String) {
                    idCity = _idCity
                    idDistrict = _idDistrict
                }
            })
    }

    private fun onClick() {

        frg_add_schedule_player_bt_from_date.setOnClickListener {

            DateTimeUtil().dialogDatePickerLight(
                activity!!,
                frg_add_schedule_player_bt_from_date,
                DateTimeUtil.DateFormatDefinition.DD_MM_YYYY.format
            )
        }

        frg_add_schedule_player_bt_from_time.setOnClickListener {

            DateTimeUtil().dialogTimePickerLight(activity!!, frg_add_schedule_player_bt_from_time)

        }

        frg_add_schedule_player_bt_to_date.setOnClickListener {

            DateTimeUtil().dialogDatePickerLight(
                activity!!,
                frg_add_schedule_player_bt_to_date,
                DateTimeUtil.DateFormatDefinition.DD_MM_YYYY.format
            )
        }

        frg_add_schedule_player_bt_to_time.setOnClickListener {

            DateTimeUtil().dialogTimePickerLight(activity!!, frg_add_schedule_player_bt_to_time)
        }

        frg_add_schedule_player_tv_input.setOnClickListener {

            startAdd()
        }
    }

    private fun enableItem(isDisable: Boolean) {

        frg_add_schedule_player_sp_city.isEnabled = isDisable
        frg_add_schedule_player_sp_district.isEnabled = isDisable
        frg_add_schedule_player_bt_from_date.isEnabled = isDisable
        frg_add_schedule_player_bt_from_time.isEnabled = isDisable
        frg_add_schedule_player_bt_to_date.isEnabled = isDisable
        frg_add_schedule_player_bt_to_time.isEnabled = isDisable

    }

    private fun startAdd() {

        val startTime =
            frg_add_schedule_player_bt_from_date.text.toString() + " " + frg_add_schedule_player_bt_from_time.text.toString()

        val endTime =
            frg_add_schedule_player_bt_to_date.text.toString() + " " + frg_add_schedule_player_bt_to_time.text.toString()

        if (DateTimeUtil().getTimeInMilliseconds(endTime, DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format)
            <= DateTimeUtil().getTimeInMilliseconds(
                startTime,
                DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format
            )
        ) {

            AlertDialog().showDialog(activity!!, Enum.EnumConfirmYes.ValidDateSchedule.value, object : ConfirmListener {
                override fun onConfirm(id: Int) {

                }
            })

            return
        }

        val userModel = getDatabase().getUserDAO().getItems()

        enableItem(false)

        showProgress(true)

        val db = FirebaseFirestore.getInstance().collection(Constant().schedulePlayerPathField)

        val currentTime = Calendar.getInstance().timeInMillis

        val id = "${Utils().getRandomNumberString()}$currentTime"
        val schedulePlayerModel = SchedulePlayerModel(
            id, idCity, idDistrict, startTime, endTime, userModel.id, userModel.name, userModel.phone, userModel.photoUrl
        )
        db.document(id!!).set(schedulePlayerModel)
            .addOnSuccessListener {

                Log.d(Constant().TAG, "add schedule player success")

                showAlertAdd(Enum.EnumConfirmYes.AddSchedulePlayerSuccess.value)

                addScheduleListener!!.onSuccess()
            }
            .addOnFailureListener {

                Log.d(Constant().TAG, "add schedule player fail : $it")

                showAlertAdd(Enum.EnumConfirmYes.AddSchedulePlayerFail.value)
            }

    }

    private fun showAlertAdd(type: Int) {

        showProgress(false)

        AlertDialog().showDialog(activity!!, type, object : ConfirmListener {
            override fun onConfirm(id: Int) {

                enableItem(true)
            }
        })
    }


}

