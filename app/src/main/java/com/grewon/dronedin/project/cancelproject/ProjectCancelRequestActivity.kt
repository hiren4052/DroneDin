package com.grewon.dronedin.project.cancelproject

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_milestone_completion_request.im_back
import kotlinx.android.synthetic.main.activity_milestone_completion_request.txt_accept
import kotlinx.android.synthetic.main.activity_milestone_completion_request.txt_reject
import kotlinx.android.synthetic.main.activity_project_cancel_request.*
import retrofit2.Retrofit
import javax.inject.Inject


class ProjectCancelRequestActivity : BaseActivity(), View.OnClickListener,
    CancelProjectRequestContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var cancelProjectRequestPresenter: CancelProjectRequestContract.Presenter


    private var jobRequestId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_cancel_request)
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
                DroneDinApp.loadingDialogMessage = getString(R.string.accepting)
                val params = CancelEndProjectStatusUpdateParams()
                params.jobCancelEndRequestId = jobRequestId
                params.requestStatus = "accept"
                params.requestType = JOB_REQUEST_TYPE.cancel.name
                cancelProjectRequestPresenter.projectStatusUpdate(params)
            }
            R.id.txt_reject -> {
                startActivityForResult(
                    Intent(
                        this,
                        ProjectCancelRejectActivity::class.java
                    ).putExtra(AppConstant.ID, jobRequestId), 11
                )
            }

        }
    }


    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
        finish()
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