package com.grewon.dronedin.project.cancelproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.milestone.milestonecancel.MilestoneCancelRejectActivity
import com.grewon.dronedin.milestone.milestonecancel.contract.CancelMilestoneRequestContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.CancelMilestoneStatusUpdateParams
import kotlinx.android.synthetic.main.activity_milestone_completion_request.*
import kotlinx.android.synthetic.main.activity_milestone_completion_request.im_back
import kotlinx.android.synthetic.main.activity_milestone_completion_request.top_image
import kotlinx.android.synthetic.main.activity_milestone_completion_request.txt_accept
import kotlinx.android.synthetic.main.activity_milestone_completion_request.txt_reject
import kotlinx.android.synthetic.main.activity_project_cancel_request.*
import retrofit2.Retrofit
import javax.inject.Inject


class ProjectCancelRequestActivity : BaseActivity(), View.OnClickListener,
    CancelMilestoneRequestContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var cancelMilestoneRequestPresenter: CancelMilestoneRequestContract.Presenter


    private var milestoneRequestId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_cancel_request)
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

        DroneDinApp.getAppInstance().loadGifImage(R.drawable.completed_job, top_image)

        milestoneRequestId = intent.getStringExtra(AppConstant.ID).toString()

        if (isPilotAccount()) {
            txt_project_note.text = getString(
                R.string.the_has_requested_to_cancel_the_project,
                getString(R.string.client)
            )
        } else {
            txt_project_note.text = getString(
                R.string.the_has_requested_to_cancel_the_project,
                getString(R.string.pilot)
            )
        }

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