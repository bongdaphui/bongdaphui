package com.bongdaphui.matchSchedule

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.addSchedulePlayer.AddSchedulePlayerScreen
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.AddScheduleListener
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.model.SchedulePlayerModel
import kotlinx.android.synthetic.main.frg_schedule_player.*
import kotlinx.android.synthetic.main.view_empty.*


class SchedulePlayerScreen : BaseFragment() {

    private lateinit var schedulePlayerAdapter: SchedulePlayerAdapter

    private var scheduleList: ArrayList<SchedulePlayerModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_schedule_player, container, false)

    }

    override fun onResume() {

        super.onResume()

        showHeader(true)

        showButtonBack(true)

        showFooter(false)

        setTitle(activity!!.resources.getString(R.string.schedule_of_you))
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindView() {

        initListSchedule()

        getData()

        onClick()

        refreshData()

    }

    private fun onClick() {
        frg_schedule_player_fab.setOnClickListener {
            addFragment(AddSchedulePlayerScreen.getInstance(object : AddScheduleListener {
                override fun onSuccess() {
                    getData()
                }

            }))
        }
    }

    private fun initListSchedule() {

        schedulePlayerAdapter = SchedulePlayerAdapter(context, scheduleList)

        frg_schedule_player_rcv.setHasFixedSize(true)
        frg_schedule_player_rcv.setItemViewCacheSize(20)

        schedulePlayerAdapter.setHasStableIds(true)

        frg_schedule_player_rcv.adapter = schedulePlayerAdapter

    }


    private fun getData() {

        showProgress(true)

        BaseRequest().getSchedulePlayer(object : GetDataListener<SchedulePlayerModel> {
            override fun onSuccess(item: SchedulePlayerModel) {
            }

            override fun onSuccess(list: ArrayList<SchedulePlayerModel>) {

                scheduleList.clear()

                hiddenRefresh()

                scheduleList.addAll(list)

                showEmptyView(false)

                schedulePlayerAdapter.notifyDataSetChanged()


                showProgress(false)

            }

            override fun onFail(message: String) {

                hiddenRefresh()

                showProgress(false)

                showEmptyView(true)
            }
        })
    }

    private fun showEmptyView(isShow: Boolean) {

        frg_schedule_player_rcv.visibility = if (isShow) View.GONE else View.VISIBLE

        view_empty.visibility = if (isShow) View.VISIBLE else View.GONE

    }

    private fun refreshData() {
        frg_schedule_player_refresh.setOnRefreshListener {

            getData()
        }
    }

    private fun hiddenRefresh() {

        if (frg_schedule_player_refresh != null && frg_schedule_player_refresh.isRefreshing) {
            frg_schedule_player_refresh.isRefreshing = false
        }
    }
}