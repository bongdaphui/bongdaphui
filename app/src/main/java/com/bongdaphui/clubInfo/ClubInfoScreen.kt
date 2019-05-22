package com.bongdaphui.clubInfo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.clubUpdate.UpdateClubScreen
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.AddDataListener
import com.bongdaphui.listener.ConfirmListener
import com.bongdaphui.listener.UpdateListener
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.model.ClubModel
import com.bongdaphui.model.RequestJoinClubModel
import com.bongdaphui.model.UserStickModel
import com.bongdaphui.player.PlayerStickAdapter
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_club_info_screen.*


class ClubInfoScreen : BaseFragment() {

    private val listStickPlayer: ArrayList<UserStickModel> = ArrayList()
    private var adapterStickPlayer: PlayerStickAdapter? = null
    private var clubModel: ClubModel? = null

    val listJoinClub: ArrayList<RequestJoinClubModel> = ArrayList()


    companion object {

        private const val CLUB_MODEL = "CLUB_MODEL"

        fun getInstance(clubModel: ClubModel): ClubInfoScreen {

            val screen = ClubInfoScreen()

            val bundle = Bundle()

            bundle.putSerializable(CLUB_MODEL, clubModel)

            screen.arguments = bundle

            return screen
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.bongdaphui.R.layout.fragment_club_info_screen, container, false)

    }

    override fun onResume() {

        super.onResume()

        showHeader(false)

        showFooter(false)

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindView() {

        val bundle = arguments

        if (bundle != null) {

            clubModel = bundle.getSerializable(CLUB_MODEL) as ClubModel?

        }

        initView()
        loadData()
        onClick()
    }

    private fun loadData() {

        clubModel?.let {
            for (item in it.players) {
                listStickPlayer.add(Gson().fromJson(item, UserStickModel::class.java))
            }
        }
        adapterStickPlayer?.notifyDataSetChanged()

    }

    @SuppressLint("RestrictedApi")
    private fun initView() {

        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        collapsing_toolbar.title = clubModel!!.name
        Glide.with(context!!).load(
            if (TextUtils.isEmpty(clubModel?.photo)) Utils().getDrawable(
                context!!,
                com.bongdaphui.R.drawable.ic_picture
            ) else clubModel?.photo
        )
            .into(frg_club_info_iv_logo)


        adapterStickPlayer = activity?.let { PlayerStickAdapter(it, listStickPlayer) }

        recycler_list_player.layoutManager = GridLayoutManager(activity, 3)
        recycler_list_player.setHasFixedSize(true)
        recycler_list_player.setItemViewCacheSize(5)
        adapterStickPlayer?.setHasStableIds(true)

        recycler_list_player.adapter = adapterStickPlayer

        if (clubModel?.idCaptain == getUIDUser()) {
            frg_club_info_fb_update.visibility = View.VISIBLE
        }

    }

    @SuppressLint("RestrictedApi")
    private fun onClick() {

        toolbar.setOnClickListener {

            onBackPressed()

        }

        if (clubModel!!.idCaptain == getUIDUser()) {

            fab_join_club.visibility = View.GONE

        } else {

            fab_join_club.setOnClickListener {

                requestJoinGroup()

            }
        }

        frg_club_info_fb_update.setOnClickListener {

            addFragment(UpdateClubScreen.getInstance(clubModel!!, object : AddDataListener {
                override fun onSuccess() {

                }

            }))
        }
    }


    private fun requestJoinGroup() {
        val idPlayer = getUIDUser()
        if (!TextUtils.isEmpty(idPlayer)) {

            val idClub = clubModel?.id ?: ""
            BaseRequest().registerJoinClub(idClub, idPlayer, object : UpdateListener {
                override fun onUpdateSuccess() {
                    //pending button
                    fab_join_club.isEnabled = false
                    showAlertSuccessJoinGroup()
                }

                override fun onUpdateFail(err: String) {
                    showAlertFailJoinGroup(err)
                }

            })
        } else {
            //require to login
            //non account
            activity?.let {
                AlertDialog().showDialog(
                    it,
                    Enum.EnumConfirmYes.FeatureNeedLogin.value,
                    object : ConfirmListener {
                        override fun onConfirm(id: Int) {
                            if (id == Enum.EnumConfirmYes.FeatureNeedLogin.value) {
                                addFragment(LoginScreen())
                            }
                        }
                    })
            }

        }
    }


    private fun showAlertFailJoinGroup(err: String) {

        showProgress(false)

        AlertDialog().showDialog(activity!!, Enum.EnumConfirmYes.RequestJoinClubFail.value, object :
            ConfirmListener {
            override fun onConfirm(id: Int) {

            }
        }, err)
    }

    private fun showAlertSuccessJoinGroup() {

        showProgress(false)

        AlertDialog().showDialog(activity!!, Enum.EnumConfirmYes.RequestJoinClubSuccess.value, object :
            ConfirmListener {
            override fun onConfirm(id: Int) {

            }
        })
    }
}


