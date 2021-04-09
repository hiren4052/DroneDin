package com.grewon.dronedin.milestone.milestonecancel

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.milestone.milestonecancel.contract.CancelMilestoneContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.CancelMilestoneParams
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_cancel_milestone.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject

class CancelMilestoneActivity : BaseActivity(), CancelMilestoneContract.View, View.OnClickListener {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var cancelMilestonePresenter: CancelMilestoneContract.Presenter

    private var milestoneId: String = ""
    private var jobId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_milestone)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_send_request.setOnClickListener(this)
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        txt_toolbar_title.text=getString(R.string.cancel_milestone)
        jobId = intent.getStringExtra(AppConstant.JOB_ID).toString()
        milestoneId = intent.getStringExtra(AppConstant.ID).toString()


        if (isPilotAccount()) {
            text_about_info.text = getString(R.string.cancel_milestone_pilot_description)
        } else {
            text_about_info.text = getString(R.string.cancel_milestone_client_description)
        }

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        cancelMilestonePresenter.attachView(this)
        cancelMilestonePresenter.attachApiInterface(retrofit)

    }

    override fun onCancelSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCancelFailed(loginParams: CancelMilestoneParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_send_request -> {
                if (ValidationUtils.isEmptyFiled(edt_description.text.toString())) {
                    DroneDinApp.getAppInstance().showToast(getString(R.string.please_enter_reason))
                } else {

                    val params = CancelMilestoneParams()
                    params.jobId = jobId
                    params.milestoneId = milestoneId
                    params.milestoneStatus = "cancel"
                    params.milestoneCancelDesc = edt_description.text.toString()

                    cancelMilestonePresenter.cancelMilestone(params)
                }
            }
            R.id.img_back -> {
                finish()
            }
        }
    }
}