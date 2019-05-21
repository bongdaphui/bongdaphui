package com.bongdaphui.scheduleClub

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.model.ScheduleClubModel
import kotlinx.android.synthetic.main.frg_schedule.*
import kotlinx.android.synthetic.main.view_empty.*


class ScheduleClubScreen : BaseFragment() {

    private lateinit var scheduleClubAdapter: ScheduleClubAdapter

    private var scheduleList: ArrayList<ScheduleClubModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_schedule, container, false)

    }

    override fun onResume() {

        super.onResume()

        showHeader(true)

        showButtonBack(true)

        showFooter(false)

        activity?.resources?.getString(R.string.schedule_of_you)?.let { setTitle(it) }
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindView() {

        initListSchedule()

        getData()

        frg_schedule_fab.visibility = View.GONE

    }

    private fun initListSchedule() {

        scheduleClubAdapter = ScheduleClubAdapter(context, scheduleList)

        frg_schedule_rcv.setHasFixedSize(true)
        frg_schedule_rcv.setItemViewCacheSize(20)

        scheduleClubAdapter.setHasStableIds(true)

        frg_schedule_rcv.adapter = scheduleClubAdapter

    }


    private fun getData() {

        showProgress(true)

        BaseRequest().getScheduleClub(object : GetDataListener<ScheduleClubModel> {
            override fun onSuccess(item: ScheduleClubModel) {
            }

            override fun onSuccess(list: ArrayList<ScheduleClubModel>) {

                scheduleList.clear()

                for (i in 0 until list.size) {

                    if (getUIDUser() == list[i].idCaptain) {

                        scheduleList.add(list[i])
                    }
                }

                scheduleList.sortBy { it.id }

                showEmptyView(false)

                scheduleClubAdapter.notifyDataSetChanged()

                showProgress(false)
            }

            override fun onFail(message: String) {

                showProgress(false)

                showEmptyView(true)
            }
        })
    }

    private fun showEmptyView(isShow: Boolean) {

        frg_schedule_rcv.visibility = if (isShow) View.GONE else View.VISIBLE

        view_empty.visibility = if (isShow) View.VISIBLE else View.GONE

    }
}