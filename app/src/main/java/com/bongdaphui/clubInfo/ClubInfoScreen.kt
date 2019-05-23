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
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.clubUpdate.UpdateClubScreen
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.AcceptListener
import com.bongdaphui.listener.AddDataListener
import com.bongdaphui.listener.OnItemClickListener
import com.bongdaphui.listener.UpdateListener
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.model.ClubModel
import com.bongdaphui.model.UserModel
import com.bongdaphui.model.UserStickModel
import com.bongdaphui.player.PlayerStickAdapter
import com.bongdaphui.profile.ProfileScreen
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.frg_club_info.*


class ClubInfoScreen : BaseFragment() {

    private val listStickPlayer: ArrayList<UserStickModel> = ArrayList()
    private var adapterStickPlayer: PlayerStickAdapter? = null
    private var clubModel: ClubModel? = null
    private var userModel: UserModel? = null

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
        return inflater.inflate(com.bongdaphui.R.layout.frg_club_info, container, false)

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
        userModel = getDatabase().getUserDAO().getItemById(getUIDUser())
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


        adapterStickPlayer = activity?.let {
            PlayerStickAdapter(it, listStickPlayer, object : OnItemClickListener<UserStickModel> {
                override fun onItemClick(item: UserStickModel, position: Int, type: Int) {
                    addFragment(ProfileScreen.getInstance(item.id))
                }

            })
        }

        recycler_list_player.layoutManager = GridLayoutManager(activity, 3)
        recycler_list_player.setHasFixedSize(true)
        recycler_list_player.setItemViewCacheSize(5)
        adapterStickPlayer?.setHasStableIds(true)

        recycler_list_player.adapter = adapterStickPlayer

        if (clubModel?.idCaptain == getUIDUser()) {
            frg_club_info_fb_update.visibility = View.VISIBLE
        }
        txt_club_info.text = "Đội trưởng: ${clubModel?.caption}\nĐịa chỉ: ${clubModel?.address},${context?.let {
            clubModel?.idCity?.let { it1 ->
                Utils().getNameCityDistrictFromId(
                    it,
                    it1, clubModel?.idDistrict
                )
            }
        }}\n${clubModel?.phone}"

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
                context?.let { it1 ->
                    AlertDialog().showCustomDialog(
                        it1,
                        activity!!.resources.getString(R.string.alert),
                        activity!!.resources.getString(R.string.join_club),
                        activity!!.resources.getString(R.string.no),
                        activity!!.resources.getString(R.string.yes),
                        object : AcceptListener {
                            override fun onAccept() {
                                requestJoinGroup()
                            }
                        }
                    )
                }
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

            clubModel?.let {
                userModel?.let { it1 ->
                    BaseRequest().registerJoinClub(it, it1, object : UpdateListener {
                        override fun onUpdateSuccess() {
                            //pending button
                            fab_join_club.isEnabled = false
                            showAlertJoinGroup(true)
                        }

                        override fun onUpdateFail(err: String) {
                            showAlertJoinGroup(false, err)

                        }

                    })
                }
            }
        } else {
            //require to login
            //non account

            activity?.let { it ->
                AlertDialog().showCustomDialog(
                    it,
                    activity!!.resources.getString(R.string.alert),
                    activity!!.resources.getString(R.string.this_feature_need_login),
                    activity!!.resources.getString(R.string.cancel),
                    activity!!.resources.getString(R.string.agree),
                    object : AcceptListener {
                        override fun onAccept() {

                            addFragment(LoginScreen())
                        }
                    }
                )
            }
        }
    }


    private fun showAlertJoinGroup(isSuccess: Boolean, err: String = "") {

        showProgress(false)

        activity?.let { it ->
            AlertDialog().showCustomDialog(
                it,
                activity!!.resources.getString(R.string.alert),
                if (isSuccess)
                    activity!!.resources.getString(R.string.request_join_club_success)
                else {
                    if (TextUtils.isEmpty(err)) activity!!.resources.getString(R.string.request_join_club_fail) else err
                },
                "",
                activity!!.resources.getString(R.string.close),
                object : AcceptListener {
                    override fun onAccept() {
                    }
                }
            )
        }
    }
}


