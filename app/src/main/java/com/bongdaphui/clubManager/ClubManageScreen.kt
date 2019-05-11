package com.bongdaphui.clubManager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.addClub.AddClubScreen
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.FireBaseSuccessListener
import com.bongdaphui.listener.ItemClickInterface
import com.bongdaphui.model.ClubModel
import com.bongdaphui.utils.Constant
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.fragment_manager_club.*


class ClubManageScreen : BaseFragment() {

    private var listFullClub: ArrayList<ClubModel> = ArrayList()
    private var listManagerClub: ArrayList<ClubModel> = ArrayList()

    var adapterManagerClub: ClubManagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_manager_club, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        showButtonBack(true)

        setTitle(activity!!.resources.getString(R.string.list_fc_manager))

        showButtonBack(false)

        showFooter(false)

    }

    override fun onBindView() {

        loadListClub()

        onClick()

    }

    private fun initAdapter() {

        adapterManagerClub = ClubManagerAdapter(context, listManagerClub, object :

            ItemClickInterface<ClubModel> {

            override fun OncItemlick(item: ClubModel, position: Int, type: Int) {

            }
        })

        frg_manager_club_rcv.adapter = adapterManagerClub
    }

    private fun loadListClub() {

        BaseRequest().loadData(activity!!, Constant().DATABASE_CLUB, object :
            FireBaseSuccessListener {
            override fun onSuccess(data: DataSnapshot) {

                if (data.exists()) {

                    listManagerClub.clear()

                    for (i in data.children) {

                        val club = i.getValue<ClubModel>(ClubModel::class.java)

                        listFullClub.add(club!!)

                        if (getUIDUser() == club.idUser) {

                            listManagerClub.add(club)
                        }
                    }
                    if (listManagerClub.size > 0) {

                        frg_manager_club_v_no_data.visibility = View.GONE
                        frg_manager_club_rcv.visibility = View.VISIBLE

                        initAdapter()

                    } else {

                        frg_manager_club_v_no_data.visibility = View.VISIBLE
                        frg_manager_club_rcv.visibility = View.GONE

                    }
                }
            }

            override fun onFail(message: String) {

                Log.d(Constant().TAG, "firebase field fail, message: $message")
            }
        })
    }

    private fun onClick() {

        frg_manager_club_tv_input.setOnClickListener {

            addFragment(AddClubScreen.getInstance(listFullClub[listFullClub.size - 1].id!!.toInt()))

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        showProgress(false)
    }

}