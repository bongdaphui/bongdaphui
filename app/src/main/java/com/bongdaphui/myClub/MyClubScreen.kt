package com.bongdaphui.myClub

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.addClub.AddClubScreen
import com.bongdaphui.addScheduleClub.AddScheduleClubScreen
import com.bongdaphui.addSchedulePlayer.AddSchedulePlayerScreen
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.clubInfo.ClubInfoScreen
import com.bongdaphui.listener.AddDataListener
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.model.ClubModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import kotlinx.android.synthetic.main.fragment_club.*
import kotlinx.android.synthetic.main.view_empty.*


class MyClubScreen : BaseFragment() {

    private var listMyClubModel: ArrayList<ClubModel> = ArrayList()

    var adapterMyClub: MyClubAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_club, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        setTitle(activity!!.resources.getString(R.string.list_fc_manager))

        showButtonBack(true)

        showFooter(false)

    }

    override fun onBindView() {

        initAdapter()

        getData()

        onClick()

    }

    private fun initAdapter() {

        adapterMyClub = MyClubAdapter(context, listMyClubModel, object :

            OnItemClickListener<ClubModel> {

            override fun onItemClick(item: ClubModel, position: Int, type: Int) {

                if (type == Enum.EnumTypeClick.Phone.value) {

                    if (item.phone!!.isNotEmpty())

                        Utils().openDial(activity!!, "${item.phone}")

                } else if (type == Enum.EnumTypeClick.AddSchedule.value) {

                    addFragment(AddScheduleClubScreen.getInstance(item))

                }else if (type == Enum.EnumTypeClick.View.value) {

                    addFragment(ClubInfoScreen.getInstance(item))
                }
            }
        })
        adapterMyClub?.setHasStableIds(true)
        frg_football_club_rcv.adapter = adapterMyClub
        frg_football_club_rcv.setHasFixedSize(true)
        frg_football_club_rcv.setItemViewCacheSize(20)
    }

    private fun getData() {

        showProgress(true)

        BaseRequest().getClubs(object : GetDataListener<ClubModel> {
            override fun onSuccess(list: ArrayList<ClubModel>) {

                listMyClubModel.clear()

                for (i in 0 until list.size) {

                    if (getUIDUser() == list[i].idCaptain) {

                        listMyClubModel.add(list[i])
                    }
                }

                if (listMyClubModel.size > 0) {

                    showEmptyView(false)

                    setListClub(listMyClubModel)

                } else {
                    showEmptyView(true)
                }

                Log.d(Constant().TAG, "club size: ${listMyClubModel.size}")
                adapterMyClub!!.notifyDataSetChanged()

                showProgress(false)
            }

            override fun onSuccess(item: ClubModel) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFail(message: String) {

                showProgress(false)

                showEmptyView(true)

                Log.d(Constant().TAG, "firebase field fail, message: $message")
            }
        })
    }


    private fun onClick() {

        frg_football_club_fab.setOnClickListener {
            addFragment(AddClubScreen.getInstance(object : AddDataListener {
                override fun onSuccess() {
                    getData()
                }

            }))
        }
    }

    private fun showEmptyView(isShow: Boolean) {

        frg_football_club_rcv.visibility = if (isShow) View.GONE else View.VISIBLE

        view_empty.visibility = if (isShow) View.VISIBLE else View.GONE

    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)
    }

}