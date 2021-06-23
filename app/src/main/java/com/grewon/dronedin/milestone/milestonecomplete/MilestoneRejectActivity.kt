package com.grewon.dronedin.milestone.milestonecomplete

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.milestone.milestonecomplete.contract.CompleteMilestoneContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.CompleteMilestoneRequest
import com.grewon.dronedin.server.params.CompleteMilestoneStatusUpdateParams
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_milestone_reject.*
import kotlinx.android.synthetic.main.activity_milestone_reject.im_back
import kotlinx.android.synthetic.main.activity_milestone_reject.top_image
import retrofit2.Retrofit
import javax.inject.Inject

class MilestoneRejectActivity : BaseActivity(), View.OnClickListener,
    CompleteMilestoneContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var completeMilestoneRequestPresenter: CompleteMilestoneContract.Presenter


    private var milestoneRequestId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_reject)
        initView()
        setClicks()
    }

    override fun onResume() {
        super.onResume()
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        completeMilestoneRequestPresenter.attachView(this)
        completeMilestoneRequestPresenter.attachApiInterface(retrofit)
    }


    private fun initView() {
        Glide.with(this).asGif().load(R.drawable.completed_job).into(top_image)
        milestoneRequestId = intent.getStringExtra(AppConstant.ID).toString()
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
                    DroneDinApp.loadingDialogMessage = getString(R.string.rejecting)
                    val params = CompleteMilestoneStatusUpdateParams()
                    params.milestoneRequestId = milestoneRequestId
                    params.milestoneRequestRejectReason = edt_reason.text.toString()
                    params.milestoneRequestStatus = "reject"
                    completeMilestoneRequestPresenter.milestoneStatusUpdate(params)
                }
            }
            R.id.txt_cancel -> {
                finish()
            }

        }
    }

    override fun onCompleteMilestoneGetSuccessFully(loginParams: CompleteMilestoneRequest) {

    }

    override fun onCompleteMilestoneGetFailed(loginParams: CommonMessageBean) {
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onCompleteMilestoneStatusSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }

    }

    override fun onCompleteMilestoneStatusFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }
}