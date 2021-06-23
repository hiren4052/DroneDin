package com.grewon.dronedin.forgotpassword

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.forgotpassword.contract.ForgotPasswordContract

import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.ForgotPasswordParams
import com.grewon.dronedin.utils.ScreenUtils
import com.grewon.dronedin.utils.TextChangeListeners
import com.grewon.dronedin.utils.TextUtils
import com.grewon.dronedin.utils.ValidationUtils

import kotlinx.android.synthetic.main.activity_forgot_password.*

import retrofit2.Retrofit
import javax.inject.Inject

class ForgotPasswordActivity : BaseActivity(), View.OnClickListener, ForgotPasswordContract.View {
    @Inject
    lateinit var forgotPasswordPresenter: ForgotPasswordContract.Presenter


    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtils.changeStatusBarColor(this, Color.WHITE)
        setContentView(R.layout.activity_forgot_password)
        textChangeListeners()
        initView()
        setClicks()
    }

    private fun textChangeListeners() {
        TextChangeListeners.editErrorTextRemover(edit_email, input_email)
    }

    private fun setClicks() {
        txt_send.setOnClickListener(this)
        im_back.setOnClickListener(this)

    }

    private fun initView() {
        DroneDinApp.getAppInstance().loadGifImage(R.drawable.forgot_password_image,top_image)
        txt_receive_code.text = TextUtils.receiveCodeColorSpannableString(this)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        forgotPasswordPresenter.attachView(this)
        forgotPasswordPresenter.attachApiInterface(retrofit)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_send -> {
                if (ValidationUtils.isEmptyFiled(edit_email.text.toString())) {
                    input_email.error = getString(R.string.please_enter_email_address)
                } else if (!ValidationUtils.isValidEmail(edit_email.text.toString())) {
                    input_email.error = getString(R.string.please_enter_valid_email_address)
                } else {
                    apiCall()
                }
            }
            R.id.im_back -> {
                finish()
            }
        }
    }

    private fun apiCall() {
        DroneDinApp.loadingDialogMessage = getString(R.string.please_wait)
        val params = ForgotPasswordParams(edit_email.text.toString())
        forgotPasswordPresenter.forgotPassword(params)
    }

    override fun onEmailSentSuccessful(response: CommonMessageBean) {
        DroneDinApp.getAppInstance().showToast(response.msg.toString())
        finish()
    }

    override fun onEmailSentFailed(loginParams: ForgotPasswordParams) {
        when {

            loginParams.userEmail != null -> {
                input_email.error = loginParams.userEmail
            }


        }

    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }
}