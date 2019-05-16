package com.bongdaphui.addSchedulePlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.clubInfo.ClubInfoScreen
import com.bongdaphui.listener.AddScheduleListener
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.utils.DateTimeUtil
import com.bongdaphui.utils.Utils
import kotlinx.android.synthetic.main.frg_add_schedule_player.*


class AddSchedulePlayerScreen : BaseFragment() {


    companion object {

        private var addScheduleListener: AddScheduleListener? = null

        fun getInstance(listener: AddScheduleListener): ClubInfoScreen {

            addScheduleListener = listener

            return ClubInfoScreen()

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
    }

    private fun initSpinner() {
        Utils().initSpinnerCity(
            activity!!,
            frg_add_schedule_player_sp_city,
            frg_add_schedule_player_sp_district,
            object :
                BaseSpinnerSelectInterface {
                override fun onSelectCity(_idCity: String, _idDistrict: String) {

                }
            })
    }

    private fun onClick() {
        frg_add_schedule_player_tv_input.setOnClickListener {

        }

        frg_add_schedule_player_spn_from_date.setOnClickListener {

            DateTimeUtil().dialogDatePickerLight(activity!!, frg_add_schedule_player_spn_from_date)

        }

        frg_add_schedule_player_spn_from_time.setOnClickListener {

            DateTimeUtil().dialogTimePickerLight(activity!!, frg_add_schedule_player_spn_from_time)

        }

        frg_add_schedule_player_spn_to_date.setOnClickListener {

            DateTimeUtil().dialogDatePickerLight(activity!!, frg_add_schedule_player_spn_to_date)
        }

        frg_add_schedule_player_spn_to_time.setOnClickListener {

            DateTimeUtil().dialogTimePickerLight(activity!!, frg_add_schedule_player_spn_to_time)
        }
    }
}

