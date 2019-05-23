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
import com.bongdaphui.dialog.AlertDialog
import com.bongdaphui.listener.AcceptListener
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.UpdateListener
import com.bongdaphui.model.UserModel
import com.bongdaphui.register.RegisterWithEmailScreen
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.Utils
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.frg_login.*
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
        return inflater.inflate(R.layout.frg_login, container, false)

    }

    override fun onResume() {

        super.onResume()

        showHeader(false)

        showFooter(false)
    }

    override fun onBindView() {

        initFacebook()

        initGoogle()

        onClick()
    }

    private fun initGoogle() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }
    }

    private fun initFacebook() {

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        buttonFacebookLogin.setReadPermissions("email", "public_profile")

        buttonFacebookLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {

                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {

                loginFail()

            }

            override fun onError(error: FacebookException) {

                loginFail()

            }
        })
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
            openFindField()
        }

        forgot_password.setOnClickListener {

            if (frg_login_with_email_et_email.text.isNullOrEmpty()
                || !Utils().validateEmail("${frg_login_with_email_et_email.text}")
            ) {

                dialogInputPass()

            } else {

                dialogResetPass("${frg_login_with_email_et_email.text}")
            }
        }
    }

    //Login email
    private fun signIn(email: String, password: String) {
        if (!validForm()) {
            return
        }
        hideKeyBoard()
        showProgress(true)

        getFireBaseAuth()!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                showProgress(false)

                if (it.isSuccessful) {

                    val user = getFireBaseAuth()!!.currentUser

                    Log.d(Constant().TAG, "login email uid: " + user!!.uid)

                    checkUserData(user.uid)

                } else {

                    loginFail()
                }
            }
            .addOnFailureListener {

                loginFail()

            }
            .addOnCanceledListener {
                loginFail()
            }
    }

    //Login fb
    private fun handleFacebookAccessToken(token: AccessToken) {

        showProgress(true)

        val credential = FacebookAuthProvider.getCredential(token.token)

        getFireBaseAuth()?.signInWithCredential(credential)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {

                    val user = getFireBaseAuth()!!.currentUser

                    Log.d(Constant().TAG, "login fb uid: " + user!!.uid)

                    checkUserData(user.uid)

                } else {

                    loginFail()
                }
            }

            ?.addOnFailureListener {

                loginFail()
            }
    }

    //Login gg
    private fun fireBaseAuthWithGoogle(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        getFireBaseAuth()!!.signInWithCredential(credential)

            .addOnCompleteListener {

                if (it.isSuccessful) {

                    val user = getFireBaseAuth()!!.currentUser

                    Log.d(Constant().TAG, "login gg uid: " + user!!.uid)

                    checkUserData(user.uid)

                } else {

                    loginFail()
                }
            }
    }

    private fun checkUserData(uid: String) {

        BaseRequest().getUserInfo(uid, object : GetDataListener<UserModel> {
            override fun onSuccess(list: ArrayList<UserModel>) {
            }

            override fun onSuccess(item: UserModel) {

                handleUpdate(item)
            }

            override fun onFail(message: String) {

                //not yes have data on fire base (will be alert update on manager screen)

                val userModel = UserModel(uid, "", "", "", "", "", "", "", "", ArrayList())

                BaseRequest().saveOrUpdateUser(userModel, object : UpdateListener {
                    override fun onUpdateSuccess() {

                        handleUpdate(userModel)

                    }

                    override fun onUpdateFail(err: String) {

                        loginFail()
                    }
                })
            }
        })
    }

    private fun handleUpdate(userModel: UserModel) {

        showProgress(false)

        //cache data
        getDatabase().getUserDAO().insert(userModel)

        openFindField()
    }

    private fun loginFail() {

        showProgress(false)

        Toast.makeText(
            activity,
            "Đăng nhập thất bại. Bạn vui lòng thực hiện lại trong menu Quản lý",
            Toast.LENGTH_SHORT
        ).show()

        FirebaseAuth.getInstance().signOut()

        openClub()
    }


    private fun openClub() {
        showProgress(false)

        openFindField()
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
                Log.d(Constant().TAG, "Google sign in failed", e)
            }
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

    private fun dialogInputPass() {
        activity?.let {
            AlertDialog().showCustomDialog(
                it,
                activity!!.resources.getString(R.string.alert),
                activity!!.resources.getString(R.string.please_enter_your_email_first),
                "",
                activity!!.resources.getString(R.string.close),
                object : AcceptListener {
                    override fun onAccept(message: String) {

                    }
                }
            )
        }
    }

    private fun dialogResetPass(email: String) {
        activity?.let {
            AlertDialog().showCustomDialog(
                it,
                activity!!.resources.getString(R.string.alert),
                String.format(activity!!.resources.getString(R.string.alert_reset_pass), email),
                activity!!.resources.getString(R.string.cancel),
                activity!!.resources.getString(R.string.agree),
                object : AcceptListener {
                    override fun onAccept(message: String) {

                        sendMail(email)
                    }
                }
            )
        }
    }

    private fun sendMail(email: String) {

        showProgress(true)

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener {

                showProgress(false)

                dialogSendMail(email, it.isSuccessful)
            }
            .addOnFailureListener {

                showProgress(false)
            }
    }

    private fun dialogSendMail(email: String, isSuccess: Boolean) {
        activity?.let {
            AlertDialog().showCustomDialog(
                it,
                activity!!.resources.getString(R.string.alert),
                if (isSuccess)
                    String.format(activity!!.resources.getString(R.string.please_check_email), email)
                else
                    activity!!.resources.getString(R.string.send_mail_fail),
                "",
                activity!!.resources.getString(R.string.agree),
                object : AcceptListener {
                    override fun onAccept(message: String) {

                    }
                }
            )
        }
    }
}