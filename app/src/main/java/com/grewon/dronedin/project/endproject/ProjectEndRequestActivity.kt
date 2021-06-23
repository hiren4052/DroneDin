package com.grewon.dronedin.project.endproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.enum.JOB_REQUEST_TYPE
import com.grewon.dronedin.project.endproject.contract.EndProjectRequestContract
import com.grewon.dronedin.review.SubmitReviewActivity
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.CancelEndProjectStatusUpdateParams
import com.grewon.dronedin.server.params.CreateDisputeParams
import kotlinx.android.synthetic.main.activity_project_end_request.*
import retrofit2.Retrofit
import javax.inject.Inject


class ProjectEndRequestActivity : BaseActivity(), View.OnClickListener,
    EndProjectRequestContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var endProjectRequestPresenter: EndProjectRequestContract.Presenter


    private var jobRequestId: String = ""
    private var jobId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_end_request)
        initView()
        setClicks()
    }

    override fun onResume() {
        super.onResume()

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        endProjectRequestPresenter.attachView(this)
        endProjectRequestPresenter.attachApiInterface(retrofit)


    }


    private fun initView() {

        DroneDinApp.getAppInstance().loadGifImage(R.drawable.completed_job, top_image)

        jobRequestId = intent.getStringExtra(AppConstant.ID).toString()
        jobId = intent.getStringExtra(AppConstant.JOB_ID).toString()

        if (isPilotAccount()) {
            txt_project_note.text = getString(
                R.string.the_has_requested_to_successfully_end_the_project,
                getString(R.string.client)
            )
        } else {
            txt_project_note.text = getString(
                R.string.the_has_requested_to_successfully_end_the_project,
                getString(R.string.pilot)
            )
        }

    }


    private fun setClicks() {
        im_back.setOnClickListener(this)
        txt_accept.setOnClickListener(this)
        txt_dispute.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.txt_accept -> {
                DroneDinApp.loadingDialogMessage = getString(R.string.accepting)
                val params = CancelEndProjectStatusUpdateParams()
                params.jobCancelEndRequestId = jobRequestId
                params.requestStatus = "accept"
                params.requestType = JOB_REQUEST_TYPE.end.name
                endProjectRequestPresenter.projectStatusUpdate(params)
            }
            R.id.txt_dispute -> {
                DroneDinApp.loadingDialogMessage = getString(R.string.creating)
                val params = CreateDisputeParams("End Project", jobId, jobRequestId)
                endProjectRequestPresenter.createDispute(params)
            }

        }
    }


    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
        finish()
    }

    override fun onEndProjectStatusSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            startActivity(
                Intent(this, SubmitReviewActivity::class.java).putExtra(
                    AppConstant.ID,
                    jobId
                )
            )
        }
    }

    override fun onEndProjectStatusFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onDisputeCreateSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onDisputeCreateFailed(loginParams: CreateDisputeParams) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }


}