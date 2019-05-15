package com.bongdaphui.clubInfo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.ConfirmListener
import com.bongdaphui.listener.FireBaseSuccessListener
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.model.ClubModel
import com.bongdaphui.model.RequestJoinClubModel
import com.bongdaphui.model.UserModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_club_info_screen.*


class ClubInfoScreen : BaseFragment() {

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
        return inflater.inflate(R.layout.fragment_club_info_screen, container, false)

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

        onClick()
    }

    private fun initView() {

        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        collapsing_toolbar.title = clubModel!!.name
        Glide.with(context!!).load(
            if (clubModel!!.photo!!.isEmpty()) Utils().getDrawable(
                context!!,
                R.drawable.ic_picture
            ) else clubModel!!.photo
        )
            .into(frg_club_info_iv_logo)

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

                checkAreYouRequest()

            }
        }
    }

    private fun checkAreYouRequest() {

        BaseRequest().loadData(activity!!, Constant().DATABASE_JOIN_CLUB, object :
            FireBaseSuccessListener {
            override fun onSuccess(data: DataSnapshot) {

                if (data.exists()) {

                    listJoinClub.clear()

                    for (i in data.children) {

                        val requestJoinClubModel = i.getValue<RequestJoinClubModel>(RequestJoinClubModel::class.java)

                        listJoinClub.add(requestJoinClubModel!!)

                    }

                    if (check()) {

                        showAlertSuccessJoinGroup()

                    } else {

                        loadUserInfo()

                    }

                }
            }

            override fun onFail(message: String) {

                showAlertFailJoinGroup()

            }
        })

    }

    private fun check(): Boolean {

        var isRequest = false

        for (i in 0 until listJoinClub.size) {
            if (listJoinClub[i].user.id == getUIDUser() && listJoinClub[i].idClub == clubModel!!.id) {
                isRequest = true
            }
        }
        return isRequest

    }

    private fun loadUserInfo() {

        showProgress(true)

        var userModel: UserModel


        BaseRequest().getUserInfo(getUIDUser(), object : GetDataListener<UserModel> {
            override fun onSuccess(list: ArrayList<UserModel>) {
            }

            override fun onSuccess(item: UserModel) {
                userModel = item
                showProgress(false)

                requestJoinGroup(userModel)

            }

            override fun onFail(message: String) {
                showAlertFailJoinGroup()
            }

        })

    }

    private fun requestJoinGroup(userModel: UserModel) {

        // Assign FirebaseDatabase instance with root database name.
        val databaseReference = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_JOIN_CLUB)

        val id = databaseReference.push().key

        val requestJoinClubModel = RequestJoinClubModel(id, clubModel!!.id, userModel)

        databaseReference.child(id!!).setValue(requestJoinClubModel)

            .addOnCompleteListener {

                showAlertSuccessJoinGroup()

            }
            .addOnFailureListener {

                showAlertFailJoinGroup()

            }
    }

    private fun showAlertFailJoinGroup() {

        showProgress(false)

        AlertDialog().showDialog(activity!!, Enum.EnumConfirmYes.RequestJoinClubFail.value, object :
            ConfirmListener {
            override fun onConfirm(id: Int) {

            }
        })
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