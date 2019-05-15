package com.bongdaphui.splash

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.login.LoginScreen
import com.bongdaphui.model.UserModel
import com.bongdaphui.updateAccount.UpdateAccountScreen
import com.bongdaphui.utils.Constant


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

        val currentUser = getFireBaseAuth()!!.currentUser

        if (null != currentUser) {

            Log.d(Constant().TAG, "user uid: ${currentUser.uid}")

            //check first update
            BaseRequest().getUserInfo(currentUser.uid,object : GetDataListener<UserModel> {
                override fun onSuccess(list: ArrayList<UserModel>) {
                }

                override fun onSuccess(item: UserModel) {
                    if (TextUtils.isEmpty(item.phone)) {
                        //update account for first time
                        replaceFragment(UpdateAccountScreen.getInstance(item), true)
                    } else {
                        openClubs()
                    }

                    Log.d(Constant().TAG, "user id: ${item.id}")
                }

                override fun onFail(message: String) {
                    //for case not found
                    val userModel = UserModel(currentUser.uid)
                    replaceFragment(UpdateAccountScreen.getInstance(userModel), true)
                }

            })

        } else {

            replaceFragment(LoginScreen(), true)

        }
    }

    override fun onBindView() {

    }
}