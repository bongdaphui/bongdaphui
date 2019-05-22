package com.bongdaphui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.model.UserModel
import com.bongdaphui.updateAccount.UpdateAccountScreen
import com.bongdaphui.utils.DateTimeUtil
import com.bongdaphui.utils.Utils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.frg_profile.*


class ProfileScreen : BaseFragment() {

    private lateinit var userModel: UserModel

    companion object {

        private var uidUser: String = ""

        fun getInstance(uid: String): ProfileScreen {

            uidUser = uid

            return ProfileScreen()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_profile, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        showButtonBack(true)

        setTitle(activity!!.resources.getString(R.string.info_user))

        showFooter(false)
    }

    @SuppressLint("RestrictedApi")
    override fun onBindView() {

        if (uidUser.isNotEmpty()) {

            getInfoUser()

            frg_profile_fb_update.visibility = View.GONE

        } else {

            frg_profile_container.visibility = View.VISIBLE

            frg_profile_fb_update.visibility = View.VISIBLE

            userModel = getDatabase().getUserDAO().getItemById(getUIDUser())

            fillData()
        }

        onClick()

    }

    private fun getInfoUser() {

        showProgress(true)

        BaseRequest().getUserInfo(uidUser, object : GetDataListener<UserModel> {

            override fun onSuccess(list: ArrayList<UserModel>) {
            }

            override fun onSuccess(item: UserModel) {

                frg_profile_container.visibility = View.VISIBLE

                showProgress(false)

                userModel = item

                fillData()
            }

            override fun onFail(message: String) {
                showProgress(false)
            }
        })
    }

    private fun onClick() {

        frg_profile_fb_update.setOnClickListener {

            addFragment(UpdateAccountScreen.getInstance(userModel))
        }

        frg_profile_tv_phone.setOnClickListener {
            Utils().openDial(activity!!, userModel.phone)
        }
    }

    private fun fillData() {

        frg_profile_tv_name.text =
            if (userModel.name.isEmpty()) activity!!.resources.getString(R.string.three_dot) else userModel.name

        if (userModel.photoUrl.isNotEmpty()) {
            activity?.let { Glide.with(it).load(userModel.photoUrl).into(frg_profile_iv_user) }
        }

        frg_profile_tv_height.text =
            if (userModel.height.isEmpty()) activity!!.resources.getString(R.string.three_dot) else "${userModel.height} cm"

        frg_profile_tv_weight.text =
            if (userModel.weight.isEmpty()) activity!!.resources.getString(R.string.three_dot) else "${userModel.weight} kg"

        frg_profile_tv_age.text =
            if (userModel.dob.isEmpty()) activity!!.resources.getString(R.string.three_dot) else DateTimeUtil().getAge(
                userModel.dob, DateTimeUtil.DateFormatDefinition.DD_MM_YYYY.format
            )

        frg_profile_tv_dob.text =
            if (userModel.dob.isEmpty()) activity!!.resources.getString(R.string.three_dot) else userModel.dob

        frg_profile_tv_email.text =
            if (userModel.email.isEmpty()) activity!!.resources.getString(R.string.three_dot) else userModel.email

        frg_profile_tv_phone.text =
            if (userModel.phone.isEmpty()) activity!!.resources.getString(R.string.three_dot) else userModel.phone

        frg_profile_tv_position.text =
            if (userModel.position.isEmpty()) activity!!.resources.getString(R.string.three_dot) else userModel.position
    }


}