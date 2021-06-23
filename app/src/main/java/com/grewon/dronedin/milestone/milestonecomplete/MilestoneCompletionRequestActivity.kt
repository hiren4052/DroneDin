package com.grewon.dronedin.milestone.milestonecomplete

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.milestone.milestonecomplete.contract.CompleteMilestoneContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.CompleteMilestoneRequest
import com.grewon.dronedin.server.JobAttachmentsBean
import com.grewon.dronedin.server.params.CompleteMilestoneStatusUpdateParams

import kotlinx.android.synthetic.main.activity_milestone_completion_request.*
import kotlinx.android.synthetic.main.activity_milestone_completion_request.image_recycle
import retrofit2.Retrofit
import javax.inject.Inject


class MilestoneCompletionRequestActivity : BaseActivity(), View.OnClickListener,
    CompleteMilestoneContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var completeMilestoneRequestPresenter: CompleteMilestoneContract.Presenter

    private var jobsImageAdapter: JobAttachmentsAdapter? = null

    private var milestoneBean: CompleteMilestoneRequest? = null
    private var milestoneRequestId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_completion_request)
        initView()
        setClicks()
    }

    override fun onResume() {
        super.onResume()

        DroneDinApp.loadingDialogMessage = getString(R.string.loading)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        completeMilestoneRequestPresenter.attachView(this)
        completeMilestoneRequestPresenter.attachApiInterface(retrofit)

        completeMilestoneRequestPresenter.getCompleteMilestoneDetail(milestoneRequestId)

    }


    private fun initView() {
        DroneDinApp.getAppInstance().loadGifImage(R.drawable.completed_job, top_image)

        milestoneRequestId = intent.getStringExtra(AppConstant.ID).toString()
    }

    private fun setAttachmentsAdapter(attachmentsList: ArrayList<JobAttachmentsBean>) {
        image_recycle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = JobAttachmentsAdapter(this)
        image_recycle.adapter = jobsImageAdapter
        jobsImageAdapter?.addItemsList(attachmentsList)
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
                val params = CompleteMilestoneStatusUpdateParams()
                params.milestoneRequestId = milestoneRequestId
                params.milestoneRequestStatus = "accept"
                completeMilestoneRequestPresenter.milestoneStatusUpdate(params)
            }
            R.id.txt_reject -> {
                startActivityForResult(
                    Intent(
                        this,
                        MilestoneRejectActivity::class.java
                    ).putExtra(AppConstant.ID, milestoneRequestId), 11
                )
            }

        }
    }

    override fun onCompleteMilestoneGetSuccessFully(loginParams: CompleteMilestoneRequest) {
        setView(loginParams)
    }

    private fun setView(response: CompleteMilestoneRequest) {
        milestoneBean = response

        txt_milestone_note.text = milestoneBean?.milestoneRequestNote

        if (response.attachment != null && response.attachment.size > 0) {
            image_recycle.visibility = View.VISIBLE
            val attachmentsList = ArrayList<JobAttachmentsBean>()
            for (item in response.attachment) {
                val attachments = JobAttachmentsBean()
                attachments.attachment = item.attachment
                attachments.attachmentId = item.attachmentId
                attachmentsList.add(attachments)
            }
            setAttachmentsAdapter(attachmentsList)

        } else {
            image_recycle.visibility = View.GONE
        }
    }

    override fun onCompleteMilestoneGetFailed(loginParams: CommonMessageBean) {
        finish()
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
        finish()
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