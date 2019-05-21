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
import com.bongdaphui.model.UserModel
import com.bongdaphui.myClub.MyClubScreen
import com.bongdaphui.profile.ProfileScreen
import com.bongdaphui.scheduleClub.ScheduleClubScreen
import com.bongdaphui.schedulePlayer.SchedulePlayerScreen
import com.bongdaphui.utils.Enum
import com.bongdaphui.utils.Tools
import com.bongdaphui.utils.ViewAnimation
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
            frg_manager_v_manager.visibility = View.GONE

        } else {

            frg_manager_bt_login_account.visibility = View.GONE
            frg_manager_v_manager.visibility = View.VISIBLE
        }
    }

    private fun onClick() {

        frg_manager_profile.setOnClickListener {
            addFragment(ProfileScreen.getInstance(""))
        }

        frg_manager_bt_toggle_info.setOnClickListener {

            toggleSection(it, frg_manager_expand_info)
        }

        frg_manager_my_club_list.setOnClickListener {
            addFragment(MyClubScreen())
        }

        frg_manager_my_club_schedule.setOnClickListener {
            addFragment(ScheduleClubScreen())
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

    private fun toggleSection(bt: View, lyt: View) {
        val show = toggleArrow(bt)
        if (show) {
            ViewAnimation.expand(lyt, object : ViewAnimation.AnimListener {
                override fun onFinish() {
                    Tools.nestedScrollTo(frg_manager_nested_scroll_view, lyt)
                }
            })
        } else {
            ViewAnimation.collapse(lyt)
        }
    }

    private fun toggleArrow(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }
}