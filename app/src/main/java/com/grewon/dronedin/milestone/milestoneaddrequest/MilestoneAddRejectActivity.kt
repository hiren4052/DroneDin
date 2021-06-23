package com.grewon.dronedin.milestone.milestoneaddrequest

import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.milestone.milestoneaddrequest.contract.NewMilestoneRequestContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.NewMilestoneRequest
import com.grewon.dronedin.server.params.NewMilestoneStatusUpdateParams
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_milestone_reject.*
import kotlinx.android.synthetic.main.activity_milestone_reject.im_back
import retrofit2.Retrofit
import javax.inject.Inject

class MilestoneAddRejectActivity : BaseActivity(), View.OnClickListener,
    NewMilestoneRequestContract.View {


    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var newMilestoneRequestPresenter: NewMilestoneRequestContract.Presenter


    private var milestoneRequestId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_add_reject)
        initView()
        setClicks()
    }

    override fun onResume() {
        super.onResume()
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        newMilestoneRequestPresenter.attachView(this)
        newMilestoneRequestPresenter.attachApiInterface(retrofit)
    }


    private fun initView() {
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

                    val params = NewMilestoneStatusUpdateParams()
                    params.milestoneRequestId = milestoneRequestId
                    params.milestoneRequestRejectReason = edt_reason.text.toString()
                    params.milestoneRequestStatus = "reject"
                    newMilestoneRequestPresenter.milestoneStatusUpdate(params)
                }
            }
            R.id.txt_cancel -> {
                finish()
            }

        }
    }

    override fun onNewMilestoneGetSuccessFully(loginParams: NewMilestoneRequest) {

    }

    override fun onNewMilestoneGetFailed(loginParams: CommonMessageBean) {
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onNewMilestoneStatusSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }

    }

    override fun onNewMilestoneStatusFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

}