package com.grewon.dronedin.milestone

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.milestone.contract.MileStoneDetailContract
import com.grewon.dronedin.paymentsummary.MilestoneSummaryActivity
import com.grewon.dronedin.paymentsummary.PaymentSummaryActivity
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.JobAttachmentsBean
import com.grewon.dronedin.server.MileStoneDetailsBean
import com.grewon.dronedin.server.MilestonesDataBean
import com.grewon.dronedin.server.params.CancelMilestoneParams
import com.grewon.dronedin.utils.TimeUtils
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_milestone_detail.*
import kotlinx.android.synthetic.main.layout_no_data.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.img_back
import retrofit2.Retrofit
import javax.inject.Inject

class MilestoneDetailActivity : BaseActivity(), View.OnClickListener, MileStoneDetailContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var mileStoneDetailPresenter: MileStoneDetailContract.Presenter

    private var jobsImageAdapter: JobAttachmentsAdapter? = null
    private var milestoneId: String = ""
    private var jobId: String = ""
    private var mileStoneDetailsBean: MileStoneDetailsBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_detail)
        initView()
        setClicks()
    }


    private fun initView() {
        txt_toolbar_title.text = getString(R.string.milestone_details)
        milestoneId = intent.getStringExtra(AppConstant.ID).toString()
        jobId = intent.getStringExtra(AppConstant.JOB_ID).toString()

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        mileStoneDetailPresenter.attachView(this)
        mileStoneDetailPresenter.attachApiInterface(retrofit)
        mileStoneDetailPresenter.getMilesStoneDetail(milestoneId)


    }


    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_pay.setOnClickListener(this)
        txt_cancel_milestone.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_pay -> {
                startActivity(
                    Intent(
                        this,
                        MilestoneSummaryActivity::class.java
                    ).putExtra(AppConstant.ID, milestoneId)
                        .putExtra(AppConstant.PRICE, mileStoneDetailsBean?.milestonePrice)
                )
            }
            R.id.txt_cancel_milestone -> {
                val params = CancelMilestoneParams()
                params.jobId = jobId
                params.milestoneId = milestoneId
                params.milestoneStatus = "cancel"
                params.milestoneCancelDesc = ""

                mileStoneDetailPresenter.cancelMilestone(params)
            }
        }
    }

    override fun onDataGetSuccessFully(loginParams: MileStoneDetailsBean) {
        if (loginParams != null) {
            setView(loginParams)
        }
    }

    private fun setView(loginParams: MileStoneDetailsBean) {
        mileStoneDetailsBean = loginParams
        txt_milestone_title.text = loginParams.milestoneDetails
        if (loginParams.milestoneStartedDate != null && !ValidationUtils.isEmptyFiled(loginParams.milestoneStartedDate)) {
            txt_started_date.text = TimeUtils.getServerToAppDate(loginParams.milestoneStartedDate)
        } else {
            started_date_layout.visibility = View.GONE
        }
        if (loginParams.milestoneCompletedDate != null && !ValidationUtils.isEmptyFiled(loginParams.milestoneCompletedDate)) {
            txt_complete_date.text =
                TimeUtils.getServerToAppDate(loginParams.milestoneCompletedDate)
        } else {
            complete_date_layout.visibility = View.GONE
        }

        if (loginParams.milestoneCancelledDate != null && !ValidationUtils.isEmptyFiled(loginParams.milestoneCancelledDate)) {
            txt_cancel_date.text = TimeUtils.getServerToAppDate(loginParams.milestoneCancelledDate)
        } else {
            cancel_date_layout.visibility = View.GONE
        }

        if (loginParams.milestoneRequestNote != null && !ValidationUtils.isEmptyFiled(loginParams.milestoneRequestNote)) {
            txt_note.text = loginParams.milestoneRequestNote
        } else {
            txt_note.visibility = View.GONE
            title_note.visibility = View.GONE
            view_layout.visibility = View.GONE
        }


        if (loginParams.milestoneStatus == AppConstant.MILESTONE_PENDING_STATUS) {
            if (isPilotAccount()) {
                txt_pay.visibility = View.GONE
            } else {
                txt_pay.visibility = View.VISIBLE
            }
        } else {
            txt_pay.visibility = View.GONE
        }
        if (loginParams.milestoneStatus == AppConstant.MILESTONE_ACTIVE_STATUS) {
            if (isPilotAccount()) {
                txt_cancel_milestone.visibility = View.GONE
            } else {
                txt_cancel_milestone.visibility = View.VISIBLE
            }
        } else {
            txt_cancel_milestone.visibility = View.GONE
        }



        txt_amount.text = getString(R.string.price_string, loginParams.milestonePrice)

        if (loginParams.attachment != null && loginParams.attachment.size > 0) {
            pictures_layout.visibility = View.VISIBLE
            val attachmentsList = ArrayList<JobAttachmentsBean>()
            for (item in loginParams.attachment) {
                val attachments = JobAttachmentsBean()
                attachments.attachment = item.attachment
                attachments.attachmentId = item.attachmentId
                attachmentsList.add(attachments)
            }
            setAttachmentsAdapter(attachmentsList)

        } else {
            pictures_layout.visibility = View.GONE
        }

    }

    override fun onDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null)
            setEmptyView(R.drawable.ic_no_data, loginParams.msg)
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


    override fun showOnScreenProgress() {
        layout_progress.visibility = View.VISIBLE
    }

    override fun hideOnScreenProgress() {
        layout_progress.visibility = View.GONE
    }

    override fun onApiException(error: Int) {
        setEmptyView(R.drawable.ic_connectivity, getString(error))
    }

    private fun setEmptyView(drawableId: Int, errorMessage: String) {
        no_data_layout.visibility = View.VISIBLE
        txt_no_data_image.setImageResource(drawableId)
        txt_no_data_title.text = errorMessage
    }


    private fun setAttachmentsAdapter(attachmentsList: ArrayList<JobAttachmentsBean>) {
        image_recycle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = JobAttachmentsAdapter(this)
        image_recycle.adapter = jobsImageAdapter
        jobsImageAdapter?.addItemsList(attachmentsList)
    }


}