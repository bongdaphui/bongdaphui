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

            signIn(
                frg_login_with_email_et_email.text.toString(),
                frg_login_with_email_et_input_password.text.toString()
            )
        }
    }

    private fun signIn(email: String, password: String) {
        if (!validForm()) {
            return
        }
        hideKeyBoard()
        showProgress(true)

        getFireBaseAuth()!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    val user = getFireBaseAuth()!!.currentUser

//                    saveUIDUser(Constant().KEY_LOGIN_UID_USER, user!!.uid)

                    Log.d(Constant().TAG, "login email uid: " + user!!.uid)

                    getInfoUser(user.uid)

                } else {

                    loginFail()
                }

                showProgress(false)
            }
            .addOnFailureListener {

                loginFail()

                showProgress(false)

            }
            .addOnCanceledListener{
                Log.d(Constant().TAG, "login email fail: ")

            }
    }

    private fun getInfoUser(id: String) {

        showProgress(true)

        BaseRequest().checkUserExistsOnFireBase(id, object : CheckUserListener {

            override fun onCheck(exists: Boolean) {

                if (exists) {

                    openClub()

                } else {

                    BaseRequest().createUserDataOnFireBase(id, object : UpdateUserListener {
                        override fun onUpdateSuccess() {

                            openClub()

                        }

                        override fun onUpdateFail() {

                            openClub()
                        }
                    })
                }
            }

            override fun onCancel() {
            }
        })
    }

    private fun openClub() {

        showProgress(false)

        openClubs()
    }

    private fun validForm(): Boolean {
        var valid = true

        if (frg_login_with_email_et_email.text.toString().isEmpty()) {

            frg_login_with_email_tv_error_input_email.visibility = View.VISIBLE
            frg_login_with_email_tv_error_input_email.text =
                activity!!.getString(R.string.please_enter_your_email)

            frg_login_with_email_et_email.requestFocus()

            valid = false
        }

        if (!Utils().validateEmail(frg_login_with_email_et_email.text.toString())) {

            frg_login_with_email_tv_error_input_email.visibility = View.VISIBLE
            frg_login_with_email_tv_error_input_email.text =
                activity!!.getString(R.string.email_not_valid)

            frg_login_with_email_et_email.requestFocus()

            valid = false

        }

        if (frg_login_with_email_et_input_password.text.toString().isEmpty()) {

            frg_login_with_email_tv_error_input_password.visibility = View.VISIBLE

            frg_login_with_email_et_input_password.requestFocus()

            valid = false

        }

        if (frg_login_with_email_et_input_password.text.toString().length < Constant().LENGTH_PASS_WORD) {

            frg_login_with_email_tv_error_input_password.visibility = View.VISIBLE

            frg_login_with_email_et_input_password.requestFocus()

            valid = false

        }

        return valid
    }

    private fun loginFail() {

        Toast.makeText(activity, "Đăng nhập thất bại, bạn vui lòng thực hiện lại", Toast.LENGTH_SHORT).show()

    }
}