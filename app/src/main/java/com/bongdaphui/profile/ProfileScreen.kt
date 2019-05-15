package com.bongdaphui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.model.RequestJoinClubModel
import com.bongdaphui.model.UserModel


class ProfileScreen : BaseFragment() {

    val listJoinClub: ArrayList<RequestJoinClubModel> = ArrayList()

    private lateinit var userModel: UserModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_manager, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        showButtonBack(false)

        setTitle(activity!!.resources.getString(R.string.info_user))

        showFooter(true)
    }

    override fun onBindView() {


    }
}