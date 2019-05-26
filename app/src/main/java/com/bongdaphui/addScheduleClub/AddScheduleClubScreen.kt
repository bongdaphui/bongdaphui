package com.bongdaphui.addScheduleClub

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.AcceptListener
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.model.ClubModel
import com.bongdaphui.model.ScheduleClubModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.DateTimeUtil
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.frg_add_schedule.*
import java.util.*


class AddScheduleClubScreen : BaseFragment() {

    private var idCity: String = ""
    private var idDistrict: String = ""

    companion object {

        private var clubModel: ClubModel? = null

        fun getInstance(model: ClubModel): AddScheduleClubScreen {

            clubModel = model

            return AddScheduleClubScreen()

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

        if (activity?.let { Utils().isNoDistrict(idDistrict, true, it) }!!) {
            return
        }

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
            activity?.let { it ->
                AlertDialog().showCustomDialog(
                    it,
                    activity!!.resources.getString(R.string.alert),
                    activity!!.resources.getString(R.string.valid_input_date_schedule),
                    "",
                    activity!!.resources.getString(R.string.close),
                    object : AcceptListener {
                        override fun onAccept(inputText: String) {
                        }
                    }
                )
            }
            return
        }

        if (!frg_add_schedule_cb_5.isChecked && !frg_add_schedule_cb_7.isChecked && !frg_add_schedule_cb_11.isChecked) {

            activity?.let { it ->
                AlertDialog().showCustomDialog(
                    it,
                    activity!!.resources.getString(R.string.alert),
                    activity!!.resources.getString(R.string.please_choose_type_field),
                    "",
                    activity!!.resources.getString(R.string.close),
                    object : AcceptListener {
                        override fun onAccept(inputText: String) {
                        }
                    }
                )
            }
            return
        }

        val typeField = StringBuilder()
        if (frg_add_schedule_cb_5.isChecked) typeField.append(Enum.EnumTypeField.FivePeople.value)
        if (frg_add_schedule_cb_7.isChecked) typeField.append(Enum.EnumTypeField.SevenPeople.value)
        if (frg_add_schedule_cb_11.isChecked) typeField.append(Enum.EnumTypeField.ElevenPeople.value)


        enableItem(false)

        showProgress(true)

        val db = FirebaseFirestore.getInstance().collection(Constant().scheduleClubPathField)

        val id =
            DateTimeUtil().getTimeInMilliseconds(startTime, DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format)

        val scheduleClubModel = ScheduleClubModel(
            "$id",
            clubModel?.id,
            clubModel?.idCaptain,
            idCity,
            idDistrict,
            startTime,
            endTime,
            clubModel?.name,
            clubModel?.phone,
            clubModel?.photo,
            typeField.toString()
        )

        val currentTime = Calendar.getInstance().timeInMillis
        val idDocument = "${Utils().getRandomNumberString()}$currentTime"

        db.document(idDocument).set(scheduleClubModel)
            .addOnSuccessListener {

                showDialogAddScheduleClub(activity!!.resources.getString(R.string.add_schedule_player_success))
            }
            .addOnFailureListener {

                Log.d(Constant().TAG, "add schedule club fail : $it")

                showDialogAddScheduleClub(activity!!.resources.getString(R.string.add_schedule_player_fail))
            }
    }

    private fun showDialogAddScheduleClub(message: String) {

        showProgress(false)

        enableItem(true)

        activity?.let { it ->
            AlertDialog().showCustomDialog(
                it,
                activity!!.resources.getString(R.string.alert),
                message,
                "",
                activity!!.resources.getString(R.string.close),
                object : AcceptListener {
                    override fun onAccept(inputText: String) {
                    }
                }
            )
        }
    }

}

