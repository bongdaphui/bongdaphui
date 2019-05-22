package com.bongdaphui.addSchedulePlayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.AddDataListener
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.listener.ConfirmListener
import com.bongdaphui.model.SchedulePlayerModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.DateTimeUtil
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.frg_add_schedule.*
import java.util.*


class AddSchedulePlayerScreen : BaseFragment() {

    private var idCity: String = ""
    private var idDistrict: String = ""

    companion object {

        private var addDataListener: AddDataListener? = null

        fun getInstance(listener: AddDataListener): AddSchedulePlayerScreen {

            addDataListener = listener

            return AddSchedulePlayerScreen()

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_add_schedule, container, false)

    }

    override fun onResume() {

        super.onResume()

        showButtonBack(true)

        showFooter(false)

        activity?.resources?.getString(R.string.add_schedule_of_you)?.let { setTitle(it) }
    }

    override fun onBindView() {

        initView()

        initSpinner()

        onClick()
    }

    private fun initView() {

        val current = Calendar.getInstance().timeInMillis

        frg_add_schedule_bt_from_date.text =
            DateTimeUtil().getFormat(current, DateTimeUtil.DateFormatDefinition.DD_MM_YYYY.format)

        frg_add_schedule_bt_from_time.text =
            DateTimeUtil().getFormat(current, DateTimeUtil.DateFormatDefinition.HH_MM.format)

        val currentPlus = Calendar.getInstance().timeInMillis + 86400000// plus 1 day

        frg_add_schedule_bt_to_date.text =
            DateTimeUtil().getFormat(currentPlus, DateTimeUtil.DateFormatDefinition.DD_MM_YYYY.format)

        frg_add_schedule_bt_to_time.text =
            DateTimeUtil().getFormat(currentPlus, DateTimeUtil.DateFormatDefinition.HH_MM.format)

    }

    private fun initSpinner() {
        activity?.let {
            Utils().initSpinnerCity(
                it,
                frg_add_schedule_sp_city, 0,
                frg_add_schedule_sp_district, 0,
                object :
                    BaseSpinnerSelectInterface {
                    override fun onSelectCity(_idCity: String, _idDistrict: String) {
                        idCity = _idCity
                        idDistrict = _idDistrict
                    }
                })
        }
    }

    private fun onClick() {

        frg_add_schedule_bt_from_date.setOnClickListener {

            activity?.let { it1 ->
                DateTimeUtil().dialogDatePickerLight(
                    it1,
                    frg_add_schedule_bt_from_date,
                    DateTimeUtil.DateFormatDefinition.DD_MM_YYYY.format
                )
            }
        }

        frg_add_schedule_bt_from_time.setOnClickListener {

            activity?.let { it1 -> DateTimeUtil().dialogTimePickerLight(it1, frg_add_schedule_bt_from_time) }

        }

        frg_add_schedule_bt_to_date.setOnClickListener {

            activity?.let { it1 ->
                DateTimeUtil().dialogDatePickerLight(
                    it1,
                    frg_add_schedule_bt_to_date,
                    DateTimeUtil.DateFormatDefinition.DD_MM_YYYY.format
                )
            }
        }

        frg_add_schedule_bt_to_time.setOnClickListener {

            activity?.let { it1 -> DateTimeUtil().dialogTimePickerLight(it1, frg_add_schedule_bt_to_time) }
        }

        frg_add_schedule_tv_input.setOnClickListener {

            startAdd()
        }
    }

    private fun enableItem(isDisable: Boolean) {

        frg_add_schedule_sp_city.isEnabled = isDisable
        frg_add_schedule_sp_district.isEnabled = isDisable
        frg_add_schedule_bt_from_date.isEnabled = isDisable
        frg_add_schedule_bt_from_time.isEnabled = isDisable
        frg_add_schedule_bt_to_date.isEnabled = isDisable
        frg_add_schedule_bt_to_time.isEnabled = isDisable

    }

    private fun startAdd() {

        val startTime =
            frg_add_schedule_bt_from_date.text.toString() + " " + frg_add_schedule_bt_from_time.text.toString()

        val endTime =
            frg_add_schedule_bt_to_date.text.toString() + " " + frg_add_schedule_bt_to_time.text.toString()

        if (DateTimeUtil().getTimeInMilliseconds(endTime, DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format)!!
            <= DateTimeUtil().getTimeInMilliseconds(
                startTime,
                DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format
            )!!
        ) {

            activity?.let {
                AlertDialog().showDialog(it, Enum.EnumConfirmYes.ValidDateSchedule.value, object : ConfirmListener {
                    override fun onConfirm(id: Int) {

                    }
                })
            }

            return
        }

        val userModel = getDatabase().getUserDAO().getItems()

        enableItem(false)

        showProgress(true)

        val db = FirebaseFirestore.getInstance().collection(Constant().schedulePlayerPathField)

        val id =
            DateTimeUtil().getTimeInMilliseconds(startTime, DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format)

        val schedulePlayerModel = SchedulePlayerModel(
            "$id",
            idCity,
            idDistrict,
            startTime,
            endTime,
            userModel.id,
            userModel.name,
            userModel.phone,
            userModel.photoUrl
        )

        val currentTime = Calendar.getInstance().timeInMillis
        val idDocument = "${Utils().getRandomNumberString()}$currentTime"

        db.document(idDocument).set(schedulePlayerModel)
            .addOnSuccessListener {

                Log.d(Constant().TAG, "add schedule player success")

                showAlertAdd(Enum.EnumConfirmYes.AddScheduleSuccess.value)

                addDataListener?.onSuccess()
            }
            .addOnFailureListener {

                Log.d(Constant().TAG, "add schedule player fail : $it")

                showAlertAdd(Enum.EnumConfirmYes.AddScheduleFail.value)
            }

    }

    private fun showAlertAdd(type: Int) {

        showProgress(false)

        activity?.let {
            AlertDialog().showDialog(it, type, object : ConfirmListener {
                override fun onConfirm(id: Int) {

                    enableItem(true)
                }
            })
        }
    }
}

