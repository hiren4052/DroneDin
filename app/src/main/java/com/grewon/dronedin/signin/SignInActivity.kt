package com.grewon.dronedin.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.addprofile.AddProfileActivity
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.forgotpassword.ForgotPasswordActivity
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.server.FacebookModel
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.LoginParams
import com.grewon.dronedin.server.params.SocialLoginParams
import com.grewon.dronedin.signin.contract.SignInContract
import com.grewon.dronedin.signup.SignUpTypeActivity
import com.grewon.dronedin.utils.TextChangeListeners
import com.grewon.dronedin.utils.TextUtils
import com.grewon.dronedin.utils.ValidationUtils
import com.grewon.dronedin.verification.VerificationActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Retrofit
import javax.inject.Inject

class SignInActivity : BaseActivity(), View.OnClickListener, SignInContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var signInPresenter: SignInContract.Presenter

    private var auth: FirebaseAuth? = null

    private var mGoogleSignInClient: GoogleSignInClient? = null

    private lateinit var callbackManager: CallbackManager

    private var loginType: String = ""


    private val RC_SIGN_IN = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        fbSetUp()
        googleSetup()
        appleSetup()
        initView()
        setClicks()
    }

    private fun initView() {
        txt_sign_up.text = TextUtils.signUpColorSpannableString(this)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        signInPresenter.attachApiInterface(retrofit)
        signInPresenter.attachView(this)

        addTextChangeListeners()

    }

    private fun addTextChangeListeners() {
        TextChangeListeners.editErrorTextRemover(edit_email, input_email)
        TextChangeListeners.editErrorTextRemover(edit_password, input_password)
    }

    private fun setClicks() {

        txt_sign_up.setOnClickListener(this)
        txt_login.setOnClickListener(this)
        txt_forgot_password.setOnClickListener(this)
        im_facebook.setOnClickListener(this)
        im_google.setOnClickListener(this)
        im_apple.setOnClickListener(this)
    }


    private fun googleSetup() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    private fun appleSetup() {
        auth = Firebase.auth
    }

    private fun appleSignIn() {
        val provider = OAuthProvider.newBuilder("apple.com")
        provider.scopes = arrayOf("email", "name").toMutableList()
        auth?.startActivityForSignInWithProvider(
            this,
            provider.build()
        )
            ?.addOnSuccessListener { authResult ->

                val user = authResult.user
                val aUSer = authResult.additionalUserInfo
                LogX.E(user?.displayName.toString())
                val firebaseUser = auth!!.currentUser
                if (user != null) {
                    socialMediaLogin(user.email.toString(), user.uid, user.displayName.toString())
                }
            }
            ?.addOnFailureListener { e ->
            }
    }


    private fun fbSetUp() {
        callbackManager = CallbackManager.Factory.create()
        fb_login_button.setPermissions(listOf("email"))
        fb_login_button.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {

                    updateUI()
                }

                override fun onCancel() {
                    // showAlert();
                }

                override fun onError(exception: FacebookException) {
                    exception.printStackTrace()
                    /* if (exception instanceof FacebookAuthorizationException) {
                            showAlert();
                        }*/
                }


            })

    }


    private fun updateUI() {
        val enableButtons = AccessToken.getCurrentAccessToken() != null
        if (enableButtons) {
            val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
            ) { `object`, response ->
                //showProgress();

                val facebookModel =
                    Gson().fromJson(`object`.toString(), FacebookModel::class.java)

                var userEmail = ""
                if (facebookModel.email != null) {
                    userEmail = facebookModel.email
                }
                socialMediaLogin(
                    userEmail,
                    facebookModel.id.toString(), facebookModel.name.toString()
                )

            }
            val parameters = Bundle()
            parameters.putString("fields", "id,name,email")
            request.parameters = parameters
            request.executeAsync()
        }

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_sign_up -> {
                startActivity(Intent(this, SignUpTypeActivity::class.java))
            }
            R.id.txt_login -> {
                if (ValidationUtils.isEmptyFiled(edit_email.text.toString())) {
                    input_email.error = getString(R.string.please_enter_email_address)
                } else if (!ValidationUtils.isValidEmail(edit_email.text.toString())) {
                    input_email.error = getString(R.string.please_enter_valid_email_address)
                } else if (ValidationUtils.isEmptyFiled(edit_password.text.toString())) {
                    input_password.error = getString(R.string.please_enter_password)
                } else {
                    apiCall()
                }

                //  apiCall()

            }
            R.id.txt_forgot_password -> {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
            }
            R.id.im_facebook -> {
                loginType = AppConstant.LOGIN_FACEBOOK
                socialSignOut()
                fb_login_button.performClick()
            }
            R.id.im_google -> {
                loginType = AppConstant.LOGIN_GOOGLE
                socialSignOut()
                googleSignIn()
            }
            R.id.im_apple -> {
                loginType = AppConstant.LOGIN_APPLE
                appleSignIn()
            }

        }

    }

    private fun apiCall() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }


            // Get new FCM registration token
            val token = task.result
            val loginParams = LoginParams(
                userDevice = "android",
                userFcmToken = token,
                userName = edit_email.text.toString(),
                userPassword = edit_password.text.toString()
            )
            signInPresenter.userLogin(loginParams)
        })

    }

    private fun socialMediaLogin(userEmail: String, socialId: String, userName: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }


            // Get new FCM registration token
            val token = task.result
            val socialLoginParams =
                SocialLoginParams(
                    userDevice = "android",
                    token,
                    loginType,
                    socialId
                )
            signInPresenter.userSocialLogin(socialLoginParams)
        })
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>?) {

        try {

            val account = completedTask?.getResult(ApiException::class.java)

            socialMediaLogin(
                account?.email.toString(),
                account?.id!!,
                account?.displayName.toString()
            )


        } catch (e: ApiException) {
            socialSignOut()
            LogX.E(e.message.toString())
            e.printStackTrace()
        }

    }

    private fun socialSignOut() {
        if (loginType == AppConstant.LOGIN_GOOGLE) {
            if (mGoogleSignInClient != null) {
                mGoogleSignInClient?.signOut()?.addOnCompleteListener {
                    return@addOnCompleteListener
                }
            }

        } else if (loginType == AppConstant.LOGIN_FACEBOOK) {

            GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                GraphRequest.Callback {
                    LoginManager.getInstance().logOut()
                }).executeAsync()

        }
    }


    override fun onUserLoggedInSuccessful(response: UserData) {
        DroneDinApp.getAppInstance().showToast(response.msg.toString())
        if (response.data != null) {

            preferenceUtils.saveAuthToken(response.data.userApiToken.toString())
            preferenceUtils.saveLoginCredential(response)

            when {
                preferenceUtils.getLoginCredentials()?.data?.userVerified?.trim() == AppConstant.NO_STATUS -> {
                    response.data.isStepComplete = false
                    preferenceUtils.saveLoginCredential(response)
                    startActivity(Intent(this, VerificationActivity::class.java))
                }
                preferenceUtils.getLoginCredentials()?.data?.profileUpdate?.trim() == AppConstant.NO_STATUS -> {
                    response.data.isStepComplete = false
                    preferenceUtils.saveLoginCredential(response)
                    startActivity(Intent(this, AddProfileActivity::class.java))
                }
                else -> {
                    response.data.isStepComplete = true
                    preferenceUtils.saveLoginCredential(response)
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
            }

        }
    }

    override fun onUserLoggedInFailed(loginParams: LoginParams) {
        when {

            loginParams.userDevice != null && !ValidationUtils.isEmptyFiled(loginParams.userDevice) -> {
                DroneDinApp.getAppInstance().showToast(loginParams.userDevice)
            }
            loginParams.userName != null -> {
                input_email.error = loginParams.userName
            }
            loginParams.userPassword != null -> {
                input_password.error = loginParams.userPassword
            }
            loginParams.userFcmToken != null -> {
                DroneDinApp.getAppInstance().showToast(loginParams.userFcmToken)
            }

        }


    }

    override fun onUserSocialLoggedInFailed(loginParams: SocialLoginParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))

        socialSignOut()
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }


}