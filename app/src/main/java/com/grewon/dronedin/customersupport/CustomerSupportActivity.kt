package com.grewon.dronedin.customersupport

import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.customersupport.contract.CustomerSupportContract
import com.grewon.dronedin.dialogs.AlertViewDialog
import com.grewon.dronedin.dialogs.SuccessDialog
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.CustomerSupportParams
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_customer_support.*
import retrofit2.Retrofit
import javax.inject.Inject

class CustomerSupportActivity : BaseActivity(), View.OnClickListener, CustomerSupportContract.View {

    private var successDialog: SuccessDialog? = null

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var supportPresenter: CustomerSupportContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_support)
        setClicks()
        initView()
    }

    private fun initView() {
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        supportPresenter.attachView(this)
        supportPresenter.attachApiInterface(retrofit)
    }

    private fun setClicks() {
        im_back.setOnClickListener(this)
        txt_send.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                onBackPressed()
            }
            R.id.txt_send -> {
                when {
                    ValidationUtils.isEmptyFiled(edt_title.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_title))
                    }
                    ValidationUtils.isEmptyFiled(edt_description.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_description))
                    }
                    else -> {
                        val params = CustomerSupportParams(
                            edt_title.text.toString(),
                            edt_description.text.toString()
                        )
                        supportPresenter.sendToSupport(params)

                    }
                }
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onSendSuccessful(response: CommonMessageBean) {
        if (response.msg != null) {
            openSuccessDialog(response.msg)
        }
    }

    override fun onSendFailed(loginParams: CustomerSupportParams) {

    }

    override fun onApiException(error: Int) {

    }

    private fun openSuccessDialog(message: String) {
        successDialog = SuccessDialog(this, R.style.DialogThme)
        successDialog!!.setTitle(message)
        successDialog!!.setPositiveBtnTxt(getString(R.string.ok))
        successDialog!!.setOkListener(View.OnClickListener {

            successDialog?.dismiss()

        })
        successDialog!!.show()
    }

}