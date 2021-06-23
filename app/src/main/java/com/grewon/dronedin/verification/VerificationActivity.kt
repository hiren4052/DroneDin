package com.grewon.dronedin.verification

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.addprofile.AddProfileActivity
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.UserIdParams
import com.grewon.dronedin.server.params.VerifyCodeParams
import com.grewon.dronedin.utils.ScreenUtils
import com.grewon.dronedin.utils.TextUtils
import com.grewon.dronedin.utils.ValidationUtils
import com.grewon.dronedin.verification.contract.VerificationContract
import kotlinx.android.synthetic.main.activity_verification.*
import retrofit2.Retrofit
import javax.inject.Inject


class VerificationActivity : BaseActivity(), View.OnClickListener, VerificationContract.View {

    @Inject
    lateinit var verificationPresenter: VerificationContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtils.changeStatusBarColor(this, Color.WHITE)
        setContentView(R.layout.activity_verification)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_submit.setOnClickListener(this)
        im_back.setOnClickListener(this)
        txt_receive_code.setOnClickListener(this)
    }

    private fun initView() {
        DroneDinApp.getAppInstance().loadGifImage(R.drawable.verifcation_image, top_image)
        txt_receive_code.text = TextUtils.receiveCodeColorSpannableString(this)
        txt_number.text = getString(
            R.string.a_verification_email_has_been_sent_to_your_email,
            preferenceUtils.getLoginCredentials()?.data?.userEmail.toString()
        )

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        verificationPresenter.attachView(this)
        verificationPresenter.attachApiInterface(retrofit)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_submit -> {
                when {
                    ValidationUtils.isEmptyFiled(otp_view.otp.toString()) -> {
                        DroneDinApp.getAppInstance().showToast(getString(R.string.please_enter_otp))
                    }
                    otp_view.otp?.length != 4 -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_valid_otp))
                    }
                    else -> {
                        DroneDinApp.loadingDialogMessage = getString(R.string.verifying)
                        val verifyCodeParams = VerifyCodeParams(
                            preferenceUtils.getLoginCredentials()?.data?.userId,
                            otp_view.otp
                        )
                        verificationPresenter.verifyUser(verifyCodeParams)
                    }
                }

            }
            R.id.txt_receive_code -> {
                DroneDinApp.loadingDialogMessage = getString(R.string.sending)
                val verifyCodeParams = UserIdParams(
                    preferenceUtils.getLoginCredentials()?.data?.userId

                )
                verificationPresenter.resendCode(verifyCodeParams)
            }
            R.id.im_back -> {
                finish()
            }
        }
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))

    }

    override fun onVerificationSuccessful(response: UserData) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)
        }
        preferenceUtils.saveAuthToken(response.data?.userApiToken.toString())
        response.data?.isStepComplete = false
        preferenceUtils.saveLoginCredential(response)
        startActivity(Intent(this, AddProfileActivity::class.java))
    }

    override fun onVerificationFailed(loginParams: VerifyCodeParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onResendCodeSuccessful(response: CommonMessageBean) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)
        }
    }

    override fun onResendCodeFailed(loginParams: UserIdParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }


}