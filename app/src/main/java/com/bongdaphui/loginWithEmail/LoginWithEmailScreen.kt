package com.bongdaphui.loginWithEmail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.CheckUserListener
import com.bongdaphui.listener.UpdateUserListener
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Utils
import kotlinx.android.synthetic.main.fragment_login_with_email_screen.*


class LoginWithEmailScreen : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_with_email_screen, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        showButtonBack(true)

        setTitle(activity!!.resources.getString(R.string.login))

        showFooter(false)

    }

    override fun onBindView() {

        frg_login_with_email_tv_login.setOnClickListener {


        }
    }


    private fun openClub() {

        showProgress(false)

        openClubs()
    }


}