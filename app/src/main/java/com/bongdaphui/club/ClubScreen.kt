package com.bongdaphui.club

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.clubInfo.ClubInfoScreen
import com.bongdaphui.listener.BaseSpinnerSelectInterface
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.model.ClubModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import kotlinx.android.synthetic.main.frg_club.*
import kotlinx.android.synthetic.main.view_empty.*


class ClubScreen : BaseFragment() {

    private var clubListFull: ArrayList<ClubModel> = ArrayList()

    private var clubList: ArrayList<ClubModel> = ArrayList()

    var clubAdapter: ClubAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.frg_club, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(false)
        showFooter(true)
    }

    @SuppressLint("RestrictedApi")
    override fun onBindView() {

        initAdapter()

        frg_club_fab.visibility = View.GONE

        loadListClub()
    }

    private fun initAdapter() {

        val isLoggedUser = !TextUtils.isEmpty(getUIDUser())

        clubAdapter = ClubAdapter(context, clubList, isLoggedUser, object :

            OnItemClickListener<ClubModel> {

            override fun onItemClick(item: ClubModel, position: Int, type: Int) {

                if (type == Enum.EnumTypeClick.Phone.value) {

                    if (isLoggedUser) {
                        Utils().openDial(activity!!, item.phone)
                    } else {
                        addFragment(LoginScreen())
                    }

                } else if (type == Enum.EnumTypeClick.View.value) {

                    addFragment(ClubInfoScreen.getInstance(item))
                }
            }
        })
        clubAdapter?.setHasStableIds(true)
        frg_club_rcv.adapter = clubAdapter
        frg_club_rcv.setHasFixedSize(true)
        frg_club_rcv.setItemViewCacheSize(20)
    }

    private fun loadListClub() {

        showProgress(true)

        BaseRequest().getClubs(object : GetDataListener<ClubModel> {
            override fun onSuccess(list: ArrayList<ClubModel>) {

                showProgress(false)

                showEmptyView(list.size == 0)

                if (list.size > 0) {

                    clubListFull.clear()

                    clubListFull.addAll(list)

                    initFilterBox()

                }
            }

            override fun onSuccess(item: ClubModel) {
            }

            override fun onFail(message: String) {

                showProgress(false)

                Log.d(Constant().TAG, "firebase field fail, message: $message")
            }
        })
    }

    private fun initFilterBox() {

        if (isAdded) {

            frg_club_cv_spinner.visibility = View.VISIBLE

            Utils().initSpinnerCity(
                activity!!,
                frg_club_sp_city, 0,
                frg_club_sp_district, 0,
                object :
                    BaseSpinnerSelectInterface {
                    override fun onSelectCity(_idCity: String, _idDistrict: String) {

                        clubList.clear()

                        for (i in 0 until clubListFull.size) {

                            if (_idCity == clubListFull[i].idCity &&
                                ("0" == _idDistrict || _idDistrict == clubListFull[i].idDistrict)
                            ) {

                                clubList.add(clubListFull[i])
                            }
                        }

                        showEmptyView(clubList.size == 0)

                        clubList.sortBy { it.idDistrict }
                        clubAdapter?.notifyDataSetChanged()
                    }
                })
        }
    }

    private fun showEmptyView(isShow: Boolean) {

        view_empty?.visibility = if (isShow) View.VISIBLE else View.GONE

        frg_club_rcv?.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)
    }

}