package com.grewon.dronedin.project.cancelproject

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.enum.JOB_REQUEST_TYPE
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.project.cancelproject.contract.CancelProjectContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.CancelEndProjectParams
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_cancel_project.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject

class CancelProjectActivity : BaseActivity(), View.OnClickListener, CancelProjectContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var cancelProjectPresenter: CancelProjectContract.Presenter


    private var jobId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_project)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_cancel_project.setOnClickListener(this)
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        jobId = intent.getStringExtra(AppConstant.ID).toString()

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        cancelProjectPresenter.attachView(this)
        cancelProjectPresenter.attachApiInterface(retrofit)

        txt_toolbar_title.text = getString(R.string.cancel_project)
        if (isPilotAccount()) {
            text_about_info.text = getString(R.string.cancel_project_pilot_description)
        } else {
            text_about_info.text = getString(R.string.cancel_project_client_description)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_cancel_project -> {
                if (ValidationUtils.isEmptyFiled(edt_description.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_description))
                } else {
                    DroneDinApp.loadingDialogMessage = getString(R.string.cancelling)
                    val params = CancelEndProjectParams()
                    params.jobId=jobId
                    params.requestType=JOB_REQUEST_TYPE.cancel.name
                    params.requestNote=edt_description.text.toString()
                    cancelProjectPresenter.cancelProject(params)
                }

            }


        }
    }

    override fun onCancelSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCancelFailed(loginParams: CancelEndProjectParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }
}