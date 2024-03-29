package com.bongdaphui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bongdaphui.R
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.UpdateListener
import com.bongdaphui.model.UserModel
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.frg_register_with_email.*


class RegisterWithEmailScreen : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_register_with_email, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        showButtonBack(true)

        setTitle(activity!!.resources.getString(R.string.register_account))

        showFooter(false)

    }

    override fun onBindView() {

        Utils().editTextTextChange(
            frg_register_with_email_et_email,
            frg_register_with_email_iv_clear_input_email,
            frg_register_with_email_tv_error_input_email
        )

        Utils().editTextTextChange(
            frg_register_with_email_et_input_password,
            frg_register_with_email_iv_clear_input_password,
            frg_register_with_email_tv_error_input_password
        )

        Utils().editTextTextChange(
            frg_register_with_email_et_input_confirm_password,
            frg_register_with_email_iv_clear_input_confirm_password,
            frg_register_with_email_tv_error_input_confirm_password
        )

        frg_register_with_email_container.setOnTouchListener { _, _ ->

            hideKeyBoard()
            false
        }


        frg_register_with_email_tv_register.setOnClickListener {

            registerAccount()
        }

    }

    private fun enableItem(isEnable: Boolean) {
        frg_register_with_email_et_email.isEnabled = isEnable
        frg_register_with_email_iv_clear_input_password.isEnabled = isEnable
        frg_register_with_email_tv_error_input_confirm_password.isEnabled = isEnable
        frg_register_with_email_tv_register.isEnabled = isEnable

    }

    private fun registerAccount() {

        if (!validForm()) {
            return
        }

        enableItem(false)

        val email = frg_register_with_email_et_email.text.toString()
        val password = frg_register_with_email_et_input_password.text.toString()

        showProgress(true)

        getFireBaseAuth()!!.createUserWithEmailAndPassword(email, password)

            .addOnCompleteListener {

                if (it.isSuccessful) {

                    val user = getFireBaseAuth()!!.currentUser

                    Log.d(Constant().tag, "register email uid: " + user!!.uid)

                    val userModel = UserModel(user.uid, "", "", email, "", "", "", "", "", ArrayList())

                    BaseRequest().saveOrUpdateUser(userModel, object : UpdateListener {
                        override fun onUpdateSuccess() {

                            showProgress(false)

                            //cache data
                            getDatabase().getUserDAO().insert(userModel)

                            openFindField()
                        }

                        override fun onUpdateFail(err: String) {

                            showProgress(false)

                            FirebaseAuth.getInstance().signOut()

                            openFindField()
                        }
                    })
                } else {

                    registerFail()
                }
            }
    }

    private fun validForm(): Boolean {

        var valid = true

        if (frg_register_with_email_et_email.text.toString().isEmpty()) {

            frg_register_with_email_tv_error_input_email.visibility = View.VISIBLE
            frg_register_with_email_tv_error_input_email.text =
                activity!!.getString(R.string.please_enter_your_email)

            frg_register_with_email_et_email.requestFocus()

            valid = false
        }

        if (!Utils().validateEmail(frg_register_with_email_et_email.text.toString())) {

            frg_register_with_email_tv_error_input_email.visibility = View.VISIBLE
            frg_register_with_email_tv_error_input_email.text =
                activity!!.getString(R.string.email_not_valid)

            frg_register_with_email_et_email.requestFocus()

            valid = false

        }

        if (frg_register_with_email_et_input_password.text.toString().isEmpty()) {

            frg_register_with_email_tv_error_input_password.visibility = View.VISIBLE
            frg_register_with_email_tv_error_input_password.text =
                activity!!.getString(R.string.please_enter_your_password)

            frg_register_with_email_et_input_password.requestFocus()

            valid = false
        }

        if (frg_register_with_email_et_input_password.text.toString().length < Constant().lengthPassWord) {

            frg_register_with_email_tv_error_input_password.visibility = View.VISIBLE
            frg_register_with_email_tv_error_input_password.text =
                activity!!.getString(R.string.invalid_password)

            frg_register_with_email_et_input_password.requestFocus()

            valid = false
        }
        if (frg_register_with_email_et_input_confirm_password.text.toString().isEmpty()) {

            frg_register_with_email_tv_error_input_confirm_password.visibility = View.VISIBLE
            frg_register_with_email_tv_error_input_confirm_password.text =
                activity!!.getString(R.string.please_re_enter_your_password)

            frg_register_with_email_et_input_confirm_password.requestFocus()

            valid = false
        }

        if (frg_register_with_email_et_input_confirm_password.text.toString()
            != frg_register_with_email_et_input_password.text.toString()
        ) {

            frg_register_with_email_tv_error_input_confirm_password.visibility = View.VISIBLE
            frg_register_with_email_tv_error_input_confirm_password.text =
                activity!!.getString(R.string.password_not_match)

            frg_register_with_email_et_input_confirm_password.requestFocus()

            valid = false
        }
        return valid
    }

    private fun registerFail() {

        showProgress(false)

        enableItem(true)

        Toast.makeText(activity, "Đăng ký thất bại, bạn vui lòng thực hiện lại", Toast.LENGTH_SHORT).show()
    }
}

