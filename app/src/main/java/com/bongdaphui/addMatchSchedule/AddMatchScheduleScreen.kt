package com.bongdaphui.addMatchSchedule

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment


class AddMatchScheduleScreen : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_add_schedule, container, false)

    }

    override fun onResume() {

        super.onResume()

        showButtonBack(true)

        showFooter(false)

        setTitle(activity!!.resources.getString(R.string.add_fc))
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindView() {

    }


}