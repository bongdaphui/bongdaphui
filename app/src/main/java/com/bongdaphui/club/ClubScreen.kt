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
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.model.ClubModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import kotlinx.android.synthetic.main.fragment_club.*


class ClubScreen : BaseFragment() {

    private var listClubModel: ArrayList<ClubModel> = ArrayList()

    var adapterClub: ClubAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_club, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        setTitle(activity!!.resources.getString(R.string.list_fc))

        showButtonBack(false)

        showFooter(true)

    }

    @SuppressLint("RestrictedApi")
    override fun onBindView() {

        initAdapter()

        frg_football_club_fab.visibility = View.GONE

        loadListClub()

    }

    private fun initAdapter() {

        val isLoggedUser = !TextUtils.isEmpty(getUIDUser())

        adapterClub = ClubAdapter(context, listClubModel, isLoggedUser, object :

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
        adapterClub?.setHasStableIds(true)
        frg_football_club_rcv.adapter = adapterClub
        frg_football_club_rcv.setHasFixedSize(true)
        frg_football_club_rcv.setItemViewCacheSize(20)
    }

    private fun loadListClub() {

        showProgress(true)
        try {

            BaseRequest().getClubs(object : GetDataListener<ClubModel> {
                override fun onSuccess(list: ArrayList<ClubModel>) {
                    listClubModel.clear()
                    listClubModel.addAll(list)
                    Log.d(Constant().TAG, "club size: ${listClubModel.size}")
                    setListClub(listClubModel)
                    adapterClub!!.notifyDataSetChanged()

                    showProgress(false)
                }

                override fun onSuccess(item: ClubModel) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onFail(message: String) {

                    showProgress(false)

                    Log.d(Constant().TAG, "firebase field fail, message: $message")
                }
            })
        } catch (e: Exception) {
            Log.d(Constant().TAG, "crash >>> ${e.message}")

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)
    }

}