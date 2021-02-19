package com.grewon.dronedin.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.forgotpassword.ForgotPasswordActivity
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.signup.SignUpTypeActivity
import com.grewon.dronedin.utils.TextUtils
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initView()
        setClicks()
    }

    private fun initView() {
        txt_sign_up.text = TextUtils.signUpColorSpannableString(this)
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
                startActivity(Intent(this, MainActivity::class.java))
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
}