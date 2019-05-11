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
import com.bongdaphui.model.CityModel
import com.bongdaphui.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

            loadListCity()
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

    private fun loadListCity() {

        val refCities = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_CITY)

        refCities.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    val listCityModel: ArrayList<CityModel> = ArrayList()

                    for (i in p0.children) {

                        val cityModel = i.getValue<CityModel>(CityModel::class.java)

                        listCityModel.add(cityModel!!)
                    }
                    try {

                        setListCity(listCityModel)

                    } catch (e: Exception) {

                        Log.d(Constant().TAG, "setListCity fail: ${e.message}")

                    }

                    openClubs()
                }
            }
        })
    }
}