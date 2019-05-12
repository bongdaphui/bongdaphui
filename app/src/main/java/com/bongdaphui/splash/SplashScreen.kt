package com.bongdaphui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.utils.Constant
import kotlinx.android.synthetic.main.fragment_splash_screen.*


class SplashScreen : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(false)

        showFooter(false)

    }

    override fun onStart() {

        super.onStart()

//        val uid = getUIDUser(Constant().KEY_LOGIN_UID_USER)
//        val uid = getUIDUser()
//
//        Log.d(Constant().TAG, "uid save: $uid")

        val currentUser = getFireBaseAuth()!!.currentUser

//        Handler().postDelayed({

        if (null != currentUser) {

            Log.d(Constant().TAG, "user uid: ${currentUser.uid}")

            openClubs()

//            loadListCity()
//                loadListField()
//                HardcodeInsertData().addDataCity()
//                HardcodeInsertUserData().addData()
//                getList()
        } else {

            replaceFragment(LoginScreen(), true)

        }
//        }, 500L)
    }

    var step1 = 0
    @SuppressLint("RestrictedApi")
    override fun onBindView() {

        fragment_fab.visibility = View.GONE
        fragment_fab.setOnClickListener {

        }
    }
}