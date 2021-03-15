package com.grewon.dronedin.signup

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
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
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.server.FacebookModel
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.RegisterParams
import com.grewon.dronedin.server.params.SocialLoginParams
import com.grewon.dronedin.server.params.SocialRegisterParams
import com.grewon.dronedin.signup.contract.SignUpContract
import com.grewon.dronedin.signup.presenter.SignUpPresenter
import com.grewon.dronedin.utils.TextChangeListeners
import com.grewon.dronedin.utils.TextUtils
import com.grewon.dronedin.utils.ValidationUtils
import com.grewon.dronedin.verification.VerificationActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Retrofit
import javax.inject.Inject


class SignUpActivity : BaseActivity(), View.OnClickListener, SignUpContract.View {


    @Inject
    lateinit var signUpPresenter: SignUpContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit


    private var auth: FirebaseAuth? = null

    private var mGoogleSignInClient: GoogleSignInClient? = null

    private lateinit var callbackManager: CallbackManager

    private var loginType: String = ""

    private val RC_SIGN_IN = 12

    private var userType: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        fbSetUp()
        googleSetup()
        appleSetup()
        initView()
        setClicks()
        addTextChangeListeners()
    }

    private fun addTextChangeListeners() {
        TextChangeListeners.editErrorTextRemover(edit_name, input_name)
        TextChangeListeners.editErrorTextRemover(edit_email, input_email)
        TextChangeListeners.editErrorTextRemover(edit_number, input_number)
        TextChangeListeners.editErrorTextRemover(edit_password, input_password)
        TextChangeListeners.editErrorTextRemover(edit_confirm_password, input_confirm_password)
    }

    private fun setClicks() {
        txt_sign_up.setOnClickListener(this)
        txt_sign_in.setOnClickListener(this)
        im_facebook.setOnClickListener(this)
        im_google.setOnClickListener(this)
        im_apple.setOnClickListener(this)
    }

    private fun initView() {

        userType = intent.getStringExtra(AppConstant.USER_TYPE).toString()



        txt_terms.text = TextUtils.appTermsSpannableString(
            this,
            getString(R.string.by_logging_in_you_agree_with_the_terms_and_conditions_learn_more)
        )
        txt_terms.movementMethod = LinkMovementMethod.getInstance()
        txt_sign_in.text = TextUtils.signInColorSpannableString(this)


        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        signUpPresenter.attachApiInterface(retrofit)
        signUpPresenter.attachView(this)

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
                LogX.E(aUSer?.profile.toString())
                LogX.E(user?.displayName.toString())
                val firebaseUser = auth!!.currentUser
                LogX.E(firebaseUser?.displayName.toString())
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
                if (ValidationUtils.isEmptyFiled(edit_name.text.toString())) {
                    input_name.error = getString(R.string.please_enter_full_name)
                } else if (ValidationUtils.isEmptyFiled(edit_email.text.toString())) {
                    edit_email.error = getString(R.string.please_enter_email_address)
                } else if (!ValidationUtils.isValidEmail(edit_email.text.toString())) {
                    edit_email.error = getString(R.string.please_enter_valid_email_address)
                } else if (!ValidationUtils.isValidEmail(edit_number.text.toString())) {
                    edit_number.error = getString(R.string.please_enter_phone_number)
                } else if (!ValidationUtils.isValidEmail(edit_password.text.toString())) {
                    edit_password.error = getString(R.string.please_enter_password)
                } else if (!ValidationUtils.isValidEmail(edit_confirm_password.text.toString())) {
                    edit_confirm_password.error = getString(R.string.please_enter_confirm_password)
                } else if (!ValidationUtils.isPasswordMatch(
                        edit_password.text.toString(),
                        edit_confirm_password.text.toString()
                    )
                ) {
                    edit_password.error = getString(R.string.password_does_not_match)
                    edit_confirm_password.error = getString(R.string.password_does_not_match)
                } else if (!check_terms.isChecked) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_check_terms_and_conditions))
                } else {

                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            return@OnCompleteListener
                        }


                        // Get new FCM registration token
                        val token = task.result
                        val registerParams = RegisterParams(
                            "yes",
                            userDeviceInfo = DroneDinApp.getAppInstance().getDeviceInformation(),
                            userEmail = edit_email.text.toString(),
                            userFcmToken = token,
                            userName = edit_name.text.toString(),
                            userPassword = edit_password.text.toString(),
                            userPhoneNumber = edit_number.text.toString(),
                            userType = userType
                        )
                        signUpPresenter.userRegister(registerParams)
                    })


                }
            }
            R.id.txt_sign_in -> {
                finish()
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


    private fun socialMediaLogin(userEmail: String, socialId: String, userName: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }


            // Get new FCM registration token
            val token = task.result
            val socialLoginParams =
                SocialRegisterParams(
                    userDeviceInfo = DroneDinApp.getAppInstance().getDeviceInformation(),
                    userEmail = userEmail,
                    userFcmToken = token,
                    userLoginType = loginType,
                    userName = userName,
                    userSocialId = socialId,
                    userType = userType

                )
            signUpPresenter.userSocialRegister(socialLoginParams)
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

    override fun onUserRegisterSuccessful(response: UserData) {
        DroneDinApp.getAppInstance().showToast(response.msg.toString())
        if (response.data != null) {
            preferenceUtils.saveAuthToken(response.data.userApiToken.toString())
            preferenceUtils.saveLoginCredential(response)
        }
    }

    override fun onUserRegisterFailed(loginParams: RegisterParams) {
        when {
            loginParams.userAgree != null -> {
                DroneDinApp.getAppInstance().showToast(loginParams.userAgree)
            }
            loginParams.userDevice != null -> {
                DroneDinApp.getAppInstance().showToast(loginParams.userDevice)
            }
            loginParams.userDeviceInfo != null -> {
                DroneDinApp.getAppInstance().showToast(loginParams.userDeviceInfo)
            }
            loginParams.userName != null -> {
                input_name.error = loginParams.userName
            }
            loginParams.userEmail != null -> {
                input_email.error = loginParams.userEmail
            }
            loginParams.userPhoneNumber != null -> {
                input_number.error = loginParams.userPhoneNumber
            }
            loginParams.userPassword != null -> {
                input_password.error = loginParams.userPassword
            }
            loginParams.userFcmToken != null -> {
                DroneDinApp.getAppInstance().showToast(loginParams.userFcmToken)
            }
            loginParams.userType != null -> {
                DroneDinApp.getAppInstance().showToast(loginParams.userType)
            }
        }
    }

    override fun onUserSocialRegisterFailed(loginParams: SocialRegisterParams) {
        val yourHashMap = Gson().fromJson(loginParams.toString(), HashMap::class.java) as HashMap<*, *>

        if (yourHashMap != null) {

            val keys: MutableSet<out Any> = yourHashMap.keys
            for (key in keys) {
                if (yourHashMap[key] != null) {
                    DroneDinApp.getAppInstance().showToast(yourHashMap[key].toString())
                    return
                }
            }
        }
        socialSignOut()

    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

}