package com.grewon.dronedin.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.evereats.app.server.UserData
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.forgotpassword.ForgotPasswordActivity
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.server.params.LoginParams
import com.grewon.dronedin.signin.contract.SignInContract
import com.grewon.dronedin.signup.SignUpTypeActivity
import com.grewon.dronedin.utils.TextUtils
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Retrofit
import javax.inject.Inject

class SignInActivity : BaseActivity(), View.OnClickListener, SignInContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var signInPresenter: SignInContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initView()
        setClicks()
    }

    private fun initView() {
        txt_sign_up.text = TextUtils.signUpColorSpannableString(this)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        signInPresenter.attachApiInterface(retrofit)
        signInPresenter.attachView(this)
    }

    private fun setClicks() {

        txt_sign_up.setOnClickListener(this)
        txt_login.setOnClickListener(this)
        txt_forgot_password.setOnClickListener(this)
        im_facebook.setOnClickListener(this)
        im_google.setOnClickListener(this)
        im_apple.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_sign_up -> {
                startActivity(Intent(this, SignUpTypeActivity::class.java))
            }
            R.id.txt_login -> {
                val loginParams=LoginParams()
                signInPresenter.userLogin(loginParams)
             //   startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.txt_forgot_password -> {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
            }
            R.id.im_facebook -> {

            }
            R.id.im_google -> {

            }
            R.id.im_apple -> {

            }

        }

    }

    override fun onUserLoggedInSuccessful(response: UserData) {

    }

    override fun onUserLoggedInFailed(error: Int) {
        LogX.E(error.toString())
    }
}