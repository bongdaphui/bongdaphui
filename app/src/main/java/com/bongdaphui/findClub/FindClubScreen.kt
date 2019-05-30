package com.bongdaphui.findClub

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bongdaphui.R
import com.bongdaphui.addScheduleClub.AddScheduleClubScreen
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.clubInfo.ClubInfoScreen
import com.bongdaphui.listener.*
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.model.ClubModel
import com.bongdaphui.model.ScheduleClubModel
import com.bongdaphui.utils.DateTimeUtil
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import kotlinx.android.synthetic.main.frg_find_club.*
import kotlinx.android.synthetic.main.view_empty.*
import java.util.*
import kotlin.collections.ArrayList


class FindClubScreen : BaseFragment() {

    private var scheduleListFull: ArrayList<ScheduleClubModel> = ArrayList()

    private var scheduleList: ArrayList<ScheduleClubModel> = ArrayList()

    private lateinit var findClubAdapter: FindClubAdapter

    private var listMyClubModel: ArrayList<ClubModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.frg_find_club, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(false)

        showFooter(true)

    }

    override fun onBindView() {

        onClick()

        initListSchedule()

        getData()

        refreshData()

        getMyClub()
    }

    private fun getMyClub() {

        BaseRequest().getClubs(object : GetDataListener<ClubModel> {
            override fun onSuccess(list: ArrayList<ClubModel>) {

                listMyClubModel.clear()

                for (i in 0 until list.size) {
                    if (getUIDUser() == list[i].idCaptain) {
                        listMyClubModel.add(list[i])
                    }
                }
            }

            override fun onSuccess(item: ClubModel) {
            }

            override fun onFail(message: String) {
            }
        })
    }

    private fun onClick() {

        frg_find_club_floatingActionButton.setOnClickListener {
            showSingleChoiceDialog()
        }
    }

    private fun showSingleChoiceDialog() {

        if (listMyClubModel.size > 0) {

            val clubs = arrayOfNulls<String>(listMyClubModel.size)

            var clubModel: ClubModel = listMyClubModel[0]

            for (i in 0 until listMyClubModel.size) {
                clubs[i] = listMyClubModel[i].name
            }

            val builder = activity?.let { AlertDialog.Builder(it) }

            builder?.setTitle("Chọn đội bóng để thêm lịch thi đấu")

            builder?.setSingleChoiceItems(clubs, 0) { _, i -> clubModel = listMyClubModel[i] }

            builder?.setPositiveButton(R.string.agree) { _, _ ->
                addFragment(AddScheduleClubScreen.getInstance(clubModel, object : AddDataListener {
                    override fun onSuccess() {
                        getData()
                    }
                }))
            }

            builder?.setNegativeButton(R.string.cancel, null)

            builder?.show()

        } else {
            activity?.let { it ->
                com.bongdaphui.dialog.AlertDialog().showCustomDialog(
                    it,
                    activity!!.resources.getString(R.string.alert),
                    activity!!.resources.getString(R.string.you_no_club),
                    "",
                    activity!!.resources.getString(R.string.agree),
                    object : AcceptListener {
                        override fun onAccept(inputText: String) {
                        }
                    }
                )
            }
        }
    }

    private fun initListSchedule() {

        val isLoggedUser = !TextUtils.isEmpty(getUIDUser())

        findClubAdapter =
            FindClubAdapter(context, isLoggedUser, scheduleList, object : OnItemClickListener<ScheduleClubModel> {
                override fun onItemClick(item: ScheduleClubModel, position: Int, type: Int) {
                    if (type == Enum.EnumTypeClick.Phone.value) {
                        if (isLoggedUser) {
                            Utils().openDial(activity!!, "${item.phone}")
                        } else {
                            addFragment(LoginScreen())
                        }
                    } else {

                        showProgress(true)

                        item.idClub?.let {
                            BaseRequest().getClubInfo(it, object : GetDataListener<ClubModel> {
                                override fun onSuccess(list: ArrayList<ClubModel>) {}

                                override fun onSuccess(item: ClubModel) {
                                    showProgress(false)
                                    addFragment(ClubInfoScreen.getInstance(item))
                                }

                                override fun onFail(message: String) {
                                    showProgress(false)
                                    Toast.makeText(activity!!, message, Toast.LENGTH_LONG)
                                        .show()
                                }
                            })
                        }
                    }
                }
            })

        frg_find_club_rcv.setHasFixedSize(true)
        frg_find_club_rcv.setItemViewCacheSize(20)

        findClubAdapter.setHasStableIds(true)

        frg_find_club_rcv.adapter = findClubAdapter
    }

    private fun getData() {

        showProgress(true)

        BaseRequest().getScheduleClub(object : GetDataListener<ScheduleClubModel> {
            override fun onSuccess(item: ScheduleClubModel) {
            }

            override fun onSuccess(list: ArrayList<ScheduleClubModel>) {

                showProgress(false)

                Utils().hiddenRefresh(frg_find_club_refresh_view)

                scheduleListFull.clear()

                scheduleListFull.addAll(list)

                showEmptyView(false)

                initFilterBox()
            }

            override fun onFail(message: String) {

                showProgress(false)

                Utils().hiddenRefresh(frg_find_club_refresh_view)

                showEmptyView(true)
            }
        })
    }

    private fun initFilterBox() {
        if (isAdded) {

            frg_find_club_cv_spinner.visibility = View.VISIBLE

            Utils().initSpinnerCity(
                activity!!,
                frg_find_club_sp_city, 0,
                frg_find_club_sp_district, 0,
                object :
                    BaseSpinnerSelectInterface {
                    override fun onSelectCity(_idCity: String, _idDistrict: String) {

                        scheduleList.clear()

                        for (i in 0 until scheduleListFull.size) {

                            if (_idCity == scheduleListFull[i].idCity
                                && ("0" == _idDistrict || _idDistrict == scheduleListFull[i].idDistrict)
//                                && getUIDUser() != scheduleListFull[i].idCaptain

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

                        findClubAdapter.notifyDataSetChanged()
                    }
                })
        }
    }

    private fun refreshData() {

        frg_find_club_refresh_view.setOnRefreshListener {

            frg_find_club_cv_spinner.visibility = View.GONE

            frg_find_club_rcv.visibility = View.GONE

            getData()
        }
    }

    private fun showEmptyView(isShow: Boolean) {

        view_empty?.visibility = if (isShow) View.VISIBLE else View.GONE

        frg_find_club_rcv?.visibility = if (isShow) View.GONE else View.VISIBLE

    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)
    }
}