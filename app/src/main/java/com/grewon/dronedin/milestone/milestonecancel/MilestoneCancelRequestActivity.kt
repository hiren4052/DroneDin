package com.grewon.dronedin.milestone.milestonecancel

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.milestone.milestonecancel.contract.CancelMilestoneRequestContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.CancelMilestoneStatusUpdateParams
import kotlinx.android.synthetic.main.activity_milestone_completion_request.*
import retrofit2.Retrofit
import javax.inject.Inject


class MilestoneCancelRequestActivity : BaseActivity(), View.OnClickListener,
    CancelMilestoneRequestContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var cancelMilestoneRequestPresenter: CancelMilestoneRequestContract.Presenter


    private var milestoneRequestId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_cancel_request)
        initView()
        setClicks()
    }

    override fun onResume() {
        super.onResume()

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        cancelMilestoneRequestPresenter.attachView(this)
        cancelMilestoneRequestPresenter.attachApiInterface(retrofit)


    }


    private fun initView() {

        milestoneRequestId = intent.getStringExtra(AppConstant.ID).toString()
    }


    private fun setClicks() {
        im_back.setOnClickListener(this)
        txt_accept.setOnClickListener(this)
        txt_reject.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.txt_accept -> {

                DroneDinApp.loadingDialogMessage = getString(R.string.accepting)
                val params = CancelMilestoneStatusUpdateParams()
                params.milestoneRequestId = milestoneRequestId
                params.milestoneRequestStatus = "accept"
                cancelMilestoneRequestPresenter.milestoneStatusUpdate(params)
            }
            R.id.txt_reject -> {
                startActivityForResult(
                    Intent(
                        this,
                        MilestoneCancelRejectActivity::class.java
                    ).putExtra(AppConstant.ID, milestoneRequestId), 11
                )
            }

        }
    }


    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
        finish()
    }

    override fun onCancelMilestoneStatusSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }

    }

    override fun onCancelMilestoneStatusFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 11) {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

}