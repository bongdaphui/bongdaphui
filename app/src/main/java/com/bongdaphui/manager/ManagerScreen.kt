package com.bongdaphui.manager

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.ConfirmListener
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.matchSchedule.SchedulePlayerScreen
import com.bongdaphui.model.UserModel
import com.bongdaphui.profile.ProfileScreen
import com.bongdaphui.utils.Enum
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.frg_manager.*


class ManagerScreen : BaseFragment() {

    private var userModel: UserModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_manager, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        showButtonBack(false)

        setTitle(activity!!.resources.getString(R.string.manager))

        showFooter(true)
    }

    override fun onBindView() {

        userModel = getDatabase().getUserDAO().getItems()

        fillData()

        checkLogin()

        onClick()

    }

    private fun fillData() {
        frg_manager_tv_name_user.text =
            if (TextUtils.isEmpty(userModel?.name)) activity?.resources?.getText(R.string.three_dot) else userModel?.name

        if (userModel?.photoUrl?.isNotEmpty()!!) {
            activity?.let {
                Glide.with(it).asBitmap().load(userModel?.photoUrl)
                    .into(frg_manager_iv_user)
            }
        }
    }

    private fun checkLogin() {

        if (TextUtils.isEmpty(getUIDUser())) {
            frg_manager_bt_login_account.visibility = View.VISIBLE
        } else {
            frg_manager_v_manager.visibility = View.VISIBLE

        }
    }

    private fun onClick() {

        frg_manager_profile.setOnClickListener {
            addFragment(ProfileScreen.getInstance(""))
        }

        frg_manager_logout.setOnClickListener {

            AlertDialog().showDialog(activity!!, Enum.EnumConfirmYes.Logout.value, object : ConfirmListener {
                override fun onConfirm(id: Int) {

                    if (id == Enum.EnumConfirmYes.Logout.value) {

                        FirebaseAuth.getInstance().signOut()

                        replaceFragment(LoginScreen(), true)
                    }
                }
            })
        }

        frg_manager_bt_login_account.setOnClickListener {
            addFragment(LoginScreen())
        }

        frg_manager_match_schedule.setOnClickListener {

            addFragment(SchedulePlayerScreen())
        }
    }
}