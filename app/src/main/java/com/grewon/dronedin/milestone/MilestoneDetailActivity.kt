package com.grewon.dronedin.milestone

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.milestone.contract.MileStoneDetailContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.JobAttachmentsBean
import com.grewon.dronedin.server.MileStoneDetailsBean
import com.grewon.dronedin.utils.TimeUtils
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_detail)
        initView()
        setClicks()
    }


    private fun initView() {
        txt_toolbar_title.text = getString(R.string.milestone_details)
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        mileStoneDetailPresenter.attachView(this)
        mileStoneDetailPresenter.attachApiInterface(retrofit)
    }


    private fun setClicks() {
        img_back.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }

        }
    }

    override fun onDataGetSuccessFully(loginParams: MileStoneDetailsBean) {
        if (loginParams != null) {
            setView(loginParams)
        }
    }

    private fun setView(loginParams: MileStoneDetailsBean) {
        txt_milestone_title.text = loginParams.milestoneDetails
        if (loginParams.milestoneStartedDate != null) {
            txt_started_date.text = TimeUtils.getServerToAppDate(loginParams.milestoneStartedDate)
        } else {
            txt_started_date.visibility = View.GONE
        }
        if (loginParams.milestoneCompletedDate != null) {
            txt_complete_date.text =
                TimeUtils.getServerToAppDate(loginParams.milestoneCompletedDate)
        } else {
            txt_complete_date.visibility = View.GONE
        }

        if (loginParams.milestoneCancelledDate != null) {
            txt_cancel_date.text = TimeUtils.getServerToAppDate(loginParams.milestoneCancelledDate)
        } else {
            txt_cancel_date.visibility = View.GONE
        }

        if (loginParams.milestoneRequestNote != null) {
            txt_note.text = loginParams.milestoneRequestNote
        } else {
            txt_note.visibility = View.GONE
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