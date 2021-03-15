package com.grewon.dronedin.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.changepassword.contract.ChangePasswordContract
import com.grewon.dronedin.forgotpassword.contract.ForgotPasswordContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.ChangePasswordParams
import com.grewon.dronedin.server.params.ForgotPasswordParams
import com.grewon.dronedin.utils.TextChangeListeners
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_change_password.im_back
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Retrofit
import javax.inject.Inject

class ChangePasswordActivity : BaseActivity(), View.OnClickListener,
    ChangePasswordContract.View {
    @Inject
    lateinit var changePasswordPresenter: ChangePasswordContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        setClicks()
        initView()
        textChangeListeners()
    }

    private fun textChangeListeners() {
    }

    private fun initView() {
        DroneDinApp.getAppInstance().getAppComponent().inject(this)

        changePasswordPresenter.attachView(this)
        changePasswordPresenter.attachApiInterface(retrofit)
    }

    private fun setClicks() {

        im_back.setOnClickListener(this)
        txt_save.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.txt_save -> {
                if (ValidationUtils.isEmptyFiled(edt_old_password.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_old_password))
                } else if (ValidationUtils.isEmptyFiled(edt_new_password.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_new_password))
                } else if (ValidationUtils.isEmptyFiled(edt_confirm_password.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_confirm_password))
                } else if (!ValidationUtils.isPasswordMatch(
                        edt_old_password.text.toString(),
                        edt_confirm_password.text.toString()
                    )
                ) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.password_does_not_match))
                } else {
                    val params = ChangePasswordParams(
                        edt_old_password.text.toString(),
                        edt_new_password.text.toString()
                    )
                    changePasswordPresenter.changePassword(params)
                }
            }
        }
    }



    override fun onChangePasswordSuccessful(response: CommonMessageBean) {
        DroneDinApp.getAppInstance().showToast(response.msg.toString())
        finish()
    }

    override fun onChangePasswordFailed(loginParams: ChangePasswordParams) {

    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

}