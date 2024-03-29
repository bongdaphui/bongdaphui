package com.bongdaphui.schedulePlayer

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
import com.bongdaphui.listener.AddDataListener
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.model.SchedulePlayerModel
import com.bongdaphui.utils.DateTimeUtil
import kotlinx.android.synthetic.main.frg_schedule.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_floating_action_button.*
import java.util.*
import kotlin.collections.ArrayList


class SchedulePlayerScreen : BaseFragment() {

    private lateinit var schedulePlayerAdapter: SchedulePlayerAdapter

    private var scheduleList: ArrayList<SchedulePlayerModel> = ArrayList()

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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindView() {

        initListSchedule()

        getData()

        onClick()

    }

    private fun onClick() {
        floatingActionButton.setOnClickListener {
            addFragment(AddSchedulePlayerScreen.getInstance(object : AddDataListener {
                override fun onSuccess() {
                    getData()
                }

            }))
        }
    }

    private fun initListSchedule() {

        schedulePlayerAdapter = SchedulePlayerAdapter(context, scheduleList)

        frg_schedule_rcv.setHasFixedSize(true)
        frg_schedule_rcv.setItemViewCacheSize(20)

        schedulePlayerAdapter.setHasStableIds(true)

        frg_schedule_rcv.adapter = schedulePlayerAdapter

    }


    private fun getData() {

        showProgress(true)

        BaseRequest().getSchedulePlayer(object : GetDataListener<SchedulePlayerModel> {
            override fun onSuccess(item: SchedulePlayerModel) {
            }

            override fun onSuccess(list: ArrayList<SchedulePlayerModel>) {

                scheduleList.clear()

                for (i in 0 until list.size) {

                    if (getUIDUser() == list[i].idPlayer

                        //check end time is valid with current time
                        && list[i].endTime?.let {
                            DateTimeUtil().getTimeInMilliseconds(
                                it,
                                DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format
                            )
                        }!! >= Calendar.getInstance().timeInMillis
                    ) {

                        scheduleList.add(list[i])
                    }
                }

                scheduleList.sortBy { it.id }

                showEmptyView(false)

                schedulePlayerAdapter.notifyDataSetChanged()

            }

            override fun onFail(message: String) {

                showEmptyView(true)
            }
        })
    }

    private fun showEmptyView(isShow: Boolean) {

        showProgress(false)

        frg_schedule_rcv.visibility = if (isShow) View.GONE else View.VISIBLE

        view_empty.visibility = if (isShow) View.VISIBLE else View.GONE

    }
}