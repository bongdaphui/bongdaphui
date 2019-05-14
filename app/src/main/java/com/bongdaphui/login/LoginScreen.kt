package com.bongdaphui.login

import android.content.Intent
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
import com.bongdaphui.register.RegisterWithEmailScreen
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Utils
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login_screen.*
import java.util.*


class LoginScreen : BaseFragment(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 12


    private val FB_FIELDS = "fields"
    private val FB_FIELDS_PARAMS = "id,name,email,gender,birthday,picture.type(large)"

    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.bongdaphui.R.layout.fragment_login_screen, container, false)

    }

    override fun onResume() {

        super.onResume()

        showHeader(false)

        showFooter(false)
    }

    override fun onBindView() {

//        FacebookSdk.sdkInitialize(activity)

        initFacebook()

        initGoogle()

        onClick()
    }

    private fun onClick() {

        frg_login_screen_ll_login_with_facebook.setOnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))

        }

        frg_login_screen_ll_login_with_google.setOnClickListener {

            buttonGoogleLogin.performClick()

            val signInIntent = mGoogleSignInClient?.signInIntent

            startActivityForResult(signInIntent, RC_SIGN_IN)

        }

        frg_login_screen_ll_sing_up_with_email.setOnClickListener {
            addFragment(RegisterWithEmailScreen())
        }


        frg_login_with_email_tv_login.setOnClickListener {
            signIn(
                frg_login_with_email_et_email.text.toString(),
                frg_login_with_email_et_input_password.text.toString()
            )
        }

        frg_login_screen_ll_skip.setOnClickListener {
            openClubs()
        }

    }

    private fun initFacebook() {

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        buttonFacebookLogin.setReadPermissions("email", "public_profile")

        buttonFacebookLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {

                Log.d(Constant().TAG, "facebook:onSuccess:$loginResult")

                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {

                Log.d(Constant().TAG, "facebook:onCancel")

                loginFail()

            }

            override fun onError(error: FacebookException) {

                Log.d(Constant().TAG, "facebook:onError", error)

                loginFail()

            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        showProgress(true)

        val credential = FacebookAuthProvider.getCredential(token.token)

        getFireBaseAuth()!!.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    val user = getFireBaseAuth()!!.currentUser

//                    saveUIDUser(Constant().KEY_LOGIN_UID_USER, user!!.uid)

                    Log.d(Constant().TAG, "login fb uid: " + user!!.uid)

                    checkForFirstUpdate(user.uid)

                } else {

                    loginFail()
                }
            }

            .addOnFailureListener {

                loginFail()
            }
    }

    private fun checkForFirstUpdate(uid: String) {
        openClub()
    }

    private fun initGoogle() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it,gso) }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)

                fireBaseAuthWithGoogle(account!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.d(Constant().TAG, "Google sign in failed", e)
                // ...
            }
        }

    }

    private fun fireBaseAuthWithGoogle(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        getFireBaseAuth()!!.signInWithCredential(credential)

            .addOnCompleteListener {

                if (it.isSuccessful) {
                    val user = getFireBaseAuth()!!.currentUser

//                    saveUIDUser(Constant().KEY_LOGIN_UID_USER, user!!.uid)

                    Log.d(Constant().TAG, "login google uid: " + user!!.uid)

                    openClubs()

                } else {

                    loginFail()
                }
            }
            .addOnFailureListener {

                loginFail()
            }
    }

    private fun loginFail() {

        Toast.makeText(activity, "Đăng nhập thất bại, bạn vui lòng thực hiện lại", Toast.LENGTH_SHORT).show()

        onBackPressed()
    }



    private fun openClub() {
        showProgress(false)

        openClubs()
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

                    checkForFirstUpdate(user.uid)

                } else {

                    loginFail()
                }

                showProgress(false)
            }
            .addOnFailureListener {

                loginFail()

                showProgress(false)

            }
            .addOnCanceledListener {
                Log.d(Constant().TAG, "login email fail: ")

            }
    }



    private fun validForm(): Boolean {

        if (frg_login_with_email_et_email.text.toString().isEmpty()) {

            frg_login_with_email_et_email.error = getString(R.string.please_enter_your_email)
            frg_login_with_email_et_email.requestFocus()
            return false
        }

        if (!Utils().validateEmail(frg_login_with_email_et_email.text.toString())) {

            frg_login_with_email_et_email.error = getString(R.string.email_not_valid)
            frg_login_with_email_et_email.requestFocus()

            return false

        }

        if (frg_login_with_email_et_input_password.text.toString().isEmpty()) {
            frg_login_with_email_et_input_password.error = getString(R.string.please_enter_your_password)
            frg_login_with_email_et_input_password.requestFocus()

            return false

        }

        if (frg_login_with_email_et_input_password.text.toString().length < Constant().LENGTH_PASS_WORD) {

            frg_login_with_email_et_input_password.error = getString(R.string.invalid_password)

            frg_login_with_email_et_input_password.requestFocus()

            return false

        }

        return true
    }
}