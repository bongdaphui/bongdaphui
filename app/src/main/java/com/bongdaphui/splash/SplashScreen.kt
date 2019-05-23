package com.bongdaphui.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.utils.Constant

class SplashScreen : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_splash, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(false)

        showFooter(false)

    }

    override fun onStart() {

        super.onStart()

        val currentUser = getFireBaseAuth()!!.currentUser

        if (null != currentUser) {

            Log.d(Constant().TAG, "splash user uid: ${currentUser.uid}")

            openClubs()

        } else {

            replaceFragment(LoginScreen(), true)
        }
    }

    override fun onBindView() {

    }
}