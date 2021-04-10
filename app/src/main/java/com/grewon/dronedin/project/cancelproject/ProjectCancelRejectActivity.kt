package com.grewon.dronedin.project.cancelproject

import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.enum.JOB_REQUEST_TYPE
import com.grewon.dronedin.project.cancelproject.contract.CancelProjectRequestContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.CancelEndProjectStatusUpdateParams
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_project_cancel_reject.*
import retrofit2.Retrofit
import javax.inject.Inject

class ProjectCancelRejectActivity : BaseActivity(), View.OnClickListener,
    CancelProjectRequestContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var cancelProjectRequestPresenter: CancelProjectRequestContract.Presenter


    private var jobRequestId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_cancel_reject)
        initView()
        setClicks()
    }

    override fun onResume() {
        super.onResume()
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        cancelProjectRequestPresenter.attachView(this)
        cancelProjectRequestPresenter.attachApiInterface(retrofit)
    }


    private fun initView() {
        jobRequestId = intent.getStringExtra(AppConstant.ID).toString()
    }


    private fun setClicks() {
        im_back.setOnClickListener(this)
        txt_send.setOnClickListener(this)
        txt_cancel.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.txt_send -> {
                if (ValidationUtils.isEmptyFiled(edt_reason.text.toString())) {
                    DroneDinApp.getAppInstance().showToast(getString(R.string.please_enter_reason))
                } else {
                    val params = CancelEndProjectStatusUpdateParams()
                    params.jobCancelEndRequestId = jobRequestId
                    params.requestStatus = "reject"
                    params.requestRejectReason = edt_reason.text.toString()
                    params.requestType = JOB_REQUEST_TYPE.cancel.name
                    cancelProjectRequestPresenter.projectStatusUpdate(params)
                }
            }
            R.id.txt_cancel -> {
                finish()
            }

        }
    }



    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onCancelProjectStatusSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCancelProjectStatusFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }


}