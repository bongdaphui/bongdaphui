package com.bongdaphui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.base.BaseRequest
import com.bongdaphui.listener.CheckUserListener
import com.bongdaphui.listener.UpdateUserListener
import com.bongdaphui.loginWithEmail.LoginWithEmailScreen
import com.bongdaphui.register.RegisterWithEmailScreen
import com.bongdaphui.utils.Constant
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
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

    private val RC_SIGN_IN = 12

    private lateinit var googleApiClient: GoogleApiClient

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

            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)

            startActivityForResult(signInIntent, RC_SIGN_IN)

        }

        frg_login_screen_ll_sing_up_with_email.setOnClickListener {
            addFragment(RegisterWithEmailScreen())
        }

        frg_login_screen_ll_login_with_email.setOnClickListener {
            addFragment(LoginWithEmailScreen())
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

                    checkUser(user.uid)

                } else {

                    loginFail()
                }
            }

            .addOnFailureListener {

                loginFail()
            }
    }

    private fun initGoogle() {

        /*val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("839377434311-efbf98vmdponkse5o10muosur0nm20sg.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient.Builder(activity!!)
            .enableAutoManage(activity!! *//* FragmentActivity *//*, 0, this *//* OnConnectionFailedListener *//*)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        buttonGoogleLogin.setScopes(gso.scopeArray)*/
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

    private fun checkUser(uid: String) {
        BaseRequest().checkUserExistsOnFireBase(uid, object : CheckUserListener {

            override fun onCheck(exists: Boolean) {

                if (exists) {

                    openClub()

                } else {

                    BaseRequest().createUserDataOnFireBase(uid, object : UpdateUserListener {
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

                openClub()
            }

        })
    }

    private fun openClub() {
        showProgress(false)

        openClubs()
    }
}