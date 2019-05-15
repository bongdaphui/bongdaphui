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
import com.bongdaphui.utils.Enum
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.frg_manager.*


class ManagerScreen : BaseFragment() {

    private lateinit var userModel: UserModel

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

        checkLogin()

        onClick()

    }

    private fun checkLogin() {

        if (TextUtils.isEmpty(getUIDUser())) {
            frg_manager_v_login.visibility = View.VISIBLE
        } else {
            frg_manager_v_manager.visibility = View.VISIBLE

        }
    }

    private fun onClick() {

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
    }


}