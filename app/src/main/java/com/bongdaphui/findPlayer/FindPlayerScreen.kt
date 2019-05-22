package com.bongdaphui.findPlayer

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.SchedulePlayerModel
import com.bongdaphui.profile.ProfileScreen
import com.bongdaphui.utils.DateTimeUtil
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import kotlinx.android.synthetic.main.frg_find_player.*
import kotlinx.android.synthetic.main.view_empty.*
import java.util.*
import kotlin.collections.ArrayList


class FindPlayerScreen : BaseFragment() {

    private var scheduleListFull: ArrayList<SchedulePlayerModel> = ArrayList()

    private var scheduleList: ArrayList<SchedulePlayerModel> = ArrayList()

    private lateinit var findPlayerAdapter: FindPlayerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.frg_find_player, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(false)

        showFooter(true)

    }

    override fun onBindView() {

        initListSchedule()

        getData()

        refreshData()

    }

    private fun initListSchedule() {

        val isLoggedUser = !TextUtils.isEmpty(getUIDUser())

        findPlayerAdapter =
            FindPlayerAdapter(context, isLoggedUser, scheduleList, object : OnItemClickListener<SchedulePlayerModel> {
                override fun onItemClick(item: SchedulePlayerModel, position: Int, type: Int) {
                    if (type == Enum.EnumTypeClick.Phone.value) {
                        if (isLoggedUser) {
                            Utils().openDial(activity!!, "${item.phonePlayer}")
                        }
                    } else {
                        addFragment(ProfileScreen.getInstance(item.idPlayer!!))
                    }
                }
            })

        frg_find_player_rcv.setHasFixedSize(true)
        frg_find_player_rcv.setItemViewCacheSize(20)

        findPlayerAdapter.setHasStableIds(true)

        frg_find_player_rcv.adapter = findPlayerAdapter

    }

    private fun getData() {

        showProgress(true)

        BaseRequest().getSchedulePlayer(object : GetDataListener<SchedulePlayerModel> {
            override fun onSuccess(item: SchedulePlayerModel) {
            }

            override fun onSuccess(list: ArrayList<SchedulePlayerModel>) {

                showProgress(false)

                Utils().hiddenRefresh(frg_find_player_refresh_view)

                scheduleListFull.clear()

                scheduleListFull.addAll(list)

                showEmptyView(false)

                initFilterBox()

            }

            override fun onFail(message: String) {

                showProgress(false)

                Utils().hiddenRefresh(frg_find_player_refresh_view)

                showEmptyView(true)
            }
        })
    }

    private fun initFilterBox() {

        if (isAdded) {

            frg_find_player_cv_spinner.visibility = View.VISIBLE

            Utils().initSpinnerCity(
                activity!!,
                frg_find_player_sp_city, 0,
                frg_find_player_sp_district, 0,
                object :
                    BaseSpinnerSelectInterface {
                    override fun onSelectCity(_idCity: String, _idDistrict: String) {

                        scheduleList.clear()

                        for (i in 0 until scheduleListFull.size) {

                            if (_idCity == scheduleListFull[i].idCity
                                && _idDistrict == scheduleListFull[i].idDistrict
                                && getUIDUser() != scheduleListFull[i].idPlayer

                                //check end time is valid with current time
                                && scheduleListFull[i].endTime?.let {
                                    DateTimeUtil().getTimeInMilliseconds(
                                        it,
                                        DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format
                                    )
                                }!! >= Calendar.getInstance().timeInMillis
                            ) {

                                scheduleList.add(scheduleListFull[i])
                            }
                        }

                        if (scheduleList.size > 0) {

                            scheduleList.sortBy { it.id }

                            showEmptyView(false)

                        } else {

                            showEmptyView(true)
                        }

                        findPlayerAdapter.notifyDataSetChanged()
                    }
                })
        }
    }

    private fun refreshData() {

        frg_find_player_refresh_view.setOnRefreshListener {

            frg_find_player_cv_spinner.visibility = View.GONE

            frg_find_player_rcv.visibility = View.GONE

            getData()
        }
    }

    private fun showEmptyView(isShow: Boolean) {

        view_empty?.visibility = if (isShow) View.VISIBLE else View.GONE

        frg_find_player_rcv?.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)
    }
}