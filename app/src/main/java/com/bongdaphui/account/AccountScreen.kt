package com.bongdaphui.account

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.clubManager.ClubManageScreen
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.*
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.model.RequestJoinClubModel
import com.bongdaphui.model.UserModel
import com.bongdaphui.updateAccount.UpdateAccountScreen
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.fragment_account_screen.*


class AccountScreen : BaseFragment() {

    val listJoinClub: ArrayList<RequestJoinClubModel> = ArrayList()

    private lateinit var userModel: UserModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_screen, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        showButtonBack(false)

        setTitle(activity!!.resources.getString(R.string.info_user))

        showFooter(true)
    }

    override fun onBindView() {

        val uIdUser = getUIDUser()
        layout_user_info.visibility = if (TextUtils.isEmpty(uIdUser)) View.GONE else View.VISIBLE

        onClick()

        getInfoUser(uIdUser)
//
//        getListRequestJoinGroup()

    }

    private fun getInfoUser(id: String) {

        BaseRequest().checkUserExistsOnFireBase(id, object : CheckUserListener {

            override fun onCheck(exists: Boolean) {

                if (exists) {

                    BaseRequest().getUserInfo(id, object : GetDataListener<UserModel> {
                        override fun onSuccess(list: ArrayList<UserModel>) {
                        }

                        override fun onSuccess(item: UserModel) {
                            userModel = item
                            updateUI(userModel)

                            Log.d(Constant().TAG, "user id: ${userModel.id}")
                        }

                        override fun onFail(message: String) {

                        }

                    })

                } else {

                    Glide.with(context!!).load(Utils().getDrawable(context!!, R.drawable.ic_profile))
                        .apply(RequestOptions.circleCropTransform())
                        .into(frg_account_iv_user)

                    frg_account_tv_name.text = activity!!.resources.getString(R.string.not_yet_update)


                }
            }

            override fun onCancel() {
            }
        })
    }

    private fun updateUI(userModel: UserModel) {

        frg_account_v_update_account.visibility = View.VISIBLE

        Glide.with(context!!).load(
            if (Utils().isEmpty(userModel.photoUrl))
                Utils().getDrawable(context!!, R.drawable.ic_profile) else userModel.photoUrl
        )
            .apply(RequestOptions.circleCropTransform())
            .into(frg_account_iv_user)

        if (Utils().isEmpty(userModel.name)) {

            frg_account_tv_name.text = "Chưa cập nhật thông tin"

        } else {

            frg_account_tv_name.text = userModel.name
        }

    }

    private fun getListRequestJoinGroup() {
        BaseRequest().loadData(activity!!, Constant().DATABASE_JOIN_CLUB, object :
            FireBaseSuccessListener {
            override fun onSuccess(data: DataSnapshot) {

                if (data.exists()) {

                    listJoinClub.clear()

                    for (i in data.children) {

                        val requestJoinClubModel = i.getValue<RequestJoinClubModel>(RequestJoinClubModel::class.java)

                        if (getUIDUser() == requestJoinClubModel!!.user.id) {

                            listJoinClub.add(requestJoinClubModel)

                        }
                    }

                    updateUIJoinClub()
                }
            }

            override fun onFail(message: String) {

            }
        })
    }

    private fun updateUIJoinClub() {

        if (listJoinClub.size > 0) {
            frg_account_v_request_join_club.visibility = View.VISIBLE
            frg_account_tv_amount_request_join_club.text = "${listJoinClub.size}"
        } else {
            frg_account_v_request_join_club.visibility = View.GONE
        }


    }

    private fun onClick() {

        frg_account_v_update_account.setOnClickListener {

            addFragment(UpdateAccountScreen.getInstance(userModel))

        }

        frg_account_v_my_club.setOnClickListener {

            addFragment(ClubManageScreen())

        }

        frg_account_v_my_member_club.setOnClickListener {

            Toast.makeText(activity, "Chức năng đang phát triển", Toast.LENGTH_LONG).show()

        }

        frg_account_v_log_out.setOnClickListener {

            AlertDialog().showDialog(activity!!, Enum.EnumConfirmYes.Logout.value, object : ConfirmListener {
                override fun onConfirm(id: Int) {

                    if (id == Enum.EnumConfirmYes.Logout.value) {

                        FirebaseAuth.getInstance().signOut()

                        replaceFragment(LoginScreen(), true)
                    }
                }
            })
        }

        btn_login_account.setOnClickListener {
            addFragment(LoginScreen())
        }
    }

}