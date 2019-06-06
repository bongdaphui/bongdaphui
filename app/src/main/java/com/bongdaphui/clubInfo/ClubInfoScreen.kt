package com.bongdaphui.clubInfo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.SharePreferenceManager
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
        return inflater.inflate(R.layout.frg_club_info, container, false)

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
        listStickPlayer.sortBy { it.position }
        adapterStickPlayer?.notifyDataSetChanged()
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    private fun initView() {

        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        collapsing_toolbar.title = clubModel!!.name

        if (!TextUtils.isEmpty(clubModel?.photo)) {
            Glide.with(context!!).asBitmap().load(clubModel?.photo).into(frg_club_info_iv_logo)
        }

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

        frg_club_info_tv_captain.text =
            if (clubModel?.caption?.isEmpty()!!) context?.resources?.getText(R.string.not_update) else clubModel?.caption

        val address = if (clubModel?.address?.isNotEmpty()!!) "${clubModel?.address}, " else ""

        frg_club_info_tv_address.text = "$address${activity?.let {
            Utils().getNameCityDistrictFromId(
                it,
                clubModel!!.idCity,
                clubModel!!.idDistrict
            )
        }}"

        frg_club_info_tv_phone.text =
            if (clubModel?.phone?.isEmpty()!!) context?.resources?.getText(R.string.not_update) else clubModel?.phone

    }

    @SuppressLint("RestrictedApi")
    private fun onClick() {

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        val userCurrentStickModel =
            UserStickModel(
                userModel?.id ?: "",
                userModel?.photoUrl ?: "",
                userModel?.name ?: "",
                userModel?.position ?: ""
            )


        if (clubModel?.idCaptain.equals(getUIDUser()) || listStickPlayer.contains(userCurrentStickModel)) {

            fab_join_club.visibility = View.GONE

        } else {
            //check preference request
            val arrRequestedIdClub = getPreferenceArrRequest()
            fab_join_club.isEnabled = !arrRequestedIdClub.contains(clubModel?.id + userModel?.id)

            fab_join_club.setOnClickListener {
                checkUser()

            }
        }

        frg_club_info_fb_update.setOnClickListener {

            showDialogWriteReview()
        }
    }


    private fun requestJoinGroup(message: String) {

        showProgress(true)

        clubModel?.let {
            userModel?.let { it1 ->
                BaseRequest().registerJoinClub(it, it1, message, object : UpdateListener {
                    override fun onUpdateSuccess() {
                        savePreferenceRequest(it.id)
                        //pending button
                        fab_join_club.isEnabled = false
                        showAlertJoinGroup(true)
                    }

                    override fun onUpdateFail(err: String) {
                        if (err.contains("chờ duyệt")) {
                            savePreferenceRequest(it.id)
                        }
                        showAlertJoinGroup(false, err)
                    }
                })
            }
        }
    }

    private fun savePreferenceRequest(id: String) {
        //save Preference
        context?.let {
            val sharePreferenceManager = SharePreferenceManager.getInstance(it)
            val type = object : TypeToken<ArrayList<String>>() {}.type
            val currentRequestStr = sharePreferenceManager.getString(Constant().keyRequestJoinTeam)

            var arrSavedRequest: ArrayList<String> =
                if (TextUtils.isEmpty(currentRequestStr)) ArrayList() else Gson().fromJson(currentRequestStr, type)
            if (!arrSavedRequest.contains(id + userModel?.id)) {
                arrSavedRequest.add(id + userModel?.id)
            }
            sharePreferenceManager.setString(Constant().keyRequestJoinTeam, Gson().toJson(arrSavedRequest))
        }
    }

    private fun getPreferenceArrRequest(): ArrayList<String> {
        //save Preference
        context?.let {
            val sharePreferenceManager = SharePreferenceManager.getInstance(it)
            val type = object : TypeToken<ArrayList<String>>() {}.type
            val currentRequestStr = sharePreferenceManager.getString(Constant().keyRequestJoinTeam)
            if (TextUtils.isEmpty(currentRequestStr)) {
                return ArrayList()
            }
            return Gson().fromJson(currentRequestStr, type)
        }
        return ArrayList()
    }


    private fun checkUser() {

        val idPlayer = getUIDUser()

        if (!TextUtils.isEmpty(idPlayer)) {

            val phoneUser = getDatabase().getUserDAO().getItemById(idPlayer).phone

            if (!TextUtils.isEmpty(phoneUser)) {

                context?.let { it1 ->
                    AlertDialog().showCustomDialog(
                        it1,
                        activity!!.resources.getString(R.string.alert),
                        activity!!.resources.getString(R.string.join_club),
                        activity!!.resources.getString(R.string.no),
                        activity!!.resources.getString(R.string.yes),

                        object : AcceptListener {
                            override fun onAccept(message: String) {

                                requestJoinGroup(message)
                            }
                        }, true
                    )
                }
            } else {
                activity?.let { it ->
                    AlertDialog().showCustomDialog(
                        it,
                        activity!!.resources.getString(R.string.alert),
                        activity!!.resources.getString(R.string.this_feature_need_update_phone),
                        "",
                        activity!!.resources.getString(R.string.agree),
                        object : AcceptListener {
                            override fun onAccept(inputText: String) {

                            }
                        }
                    )
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
                        override fun onAccept(inputText: String) {

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
                    override fun onAccept(inputText: String) {
                    }
                }
            )
        }
    }

    private fun showDialogWriteReview() {
        context?.let { it1 ->
            AlertDialog().showCustomDialog(
                it1,
                activity!!.resources.getString(R.string.write_review),
                "",
                activity!!.resources.getString(R.string.cancel),
                activity!!.resources.getString(R.string.submit_review),

                object : AcceptListener {
                    override fun onAccept(message: String) {

                        submitReview(message)

                    }
                }, true
            )
        }
    }

    private fun submitReview(message: String) {
        showProgress(true)

        clubModel?.let {
            userModel?.let { it1 ->
                BaseRequest().writeReviewClub(it, it1, message, object : UpdateListener {
                    override fun onUpdateSuccess() {
                        showProgress(false)
                    }

                    override fun onUpdateFail(err: String) {
                        showProgress(false)

                        Toast.makeText(
                            activity,
                            "Có lỗi trong quá trình gửi đánh giá, bạn vui lòng thực hiện lại!",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                })
            }
        }
    }
}


