package com.grewon.dronedin.proposals

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.milestone.adapter.MileStoneAdapter
import com.grewon.dronedin.proposals.contract.ProposalsDetailContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.JobAttachmentsBean
import com.grewon.dronedin.server.MilestonesDataBean
import com.grewon.dronedin.server.ProposalsDetailBean
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_proposals_detail.*
import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Retrofit
import javax.inject.Inject

class ProposalsDetailActivity : BaseActivity(), View.OnClickListener, ProposalsDetailContract.View {

    @Inject
    lateinit var proposalDetailsPresenter: ProposalsDetailContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit


    private var mileStoneAdapter: MileStoneAdapter? = null
    private var jobsImageAdapter: JobAttachmentsAdapter? = null
    private var proposalId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposals_detail)
        initView()
        setClicks()
    }


    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_withdraw_proposal.setOnClickListener(this)
    }

    private fun initView() {
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        proposalDetailsPresenter.attachView(this)
        proposalDetailsPresenter.attachApiInterface(retrofit)

        proposalId = intent.getStringExtra(AppConstant.ID).toString()

        proposalDetailsPresenter.getProposalsDetails(proposalId, "proposal")

    }

    private fun setAttachmentsAdapter(attachmentsList: ArrayList<JobAttachmentsBean>) {
        image_recycle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = JobAttachmentsAdapter(this)
        image_recycle.adapter = jobsImageAdapter
        jobsImageAdapter?.addItemsList(attachmentsList)
    }

    private fun setMileStoneAdapter(milestoneList: ArrayList<MilestonesDataBean>) {
        mile_stone_recycle.layoutManager = LinearLayoutManager(this)
        mileStoneAdapter = MileStoneAdapter(this)
        mile_stone_recycle.adapter = mileStoneAdapter
        mileStoneAdapter?.addItemsList(milestoneList)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_withdraw_proposal -> {
                DroneDinApp.getAppInstance().setDialogMessage(getString(R.string.please_wait))
                proposalDetailsPresenter.withDrawProposals(proposalId)
            }
        }
    }

    override fun onProposalsDetailSuccessfully(response: ProposalsDetailBean) {
        if (response != null) {
            setView(response)
        }
    }

    private fun setView(response: ProposalsDetailBean) {
        txt_subtitle.text = response.category?.categoryName
        txt_job_title.text = response.proposalTitle
        txt_job_description.text = response.proposalDescription
        txt_job_location.text = response.jobAddress
        txt_client_name.text = response.userName
        txt_budget.text = getString(R.string.price_string, response.proposalTotalPrice)
        txt_job_date.text = TimeUtils.getServerToAppDate(response.date.toString())



        if (response.milestone != null && response.milestone.size > 0) {
            milestone_layout.visibility = View.VISIBLE
            val milestoneList = ArrayList<MilestonesDataBean>()
            for (item in response.milestone) {
                val milesStone = MilestonesDataBean()
                milesStone.milestoneDetails = item?.milestoneDetails
                milesStone.milestonePrice = item?.milestonePrice
                milesStone.milestoneId = item?.milestoneId
                milestoneList.add(milesStone)
            }
            setMileStoneAdapter(milestoneList)
        } else {
            milestone_layout.visibility = View.GONE
        }

        if (response.attachment != null && response.attachment.size > 0) {
            pictures_layout.visibility = View.VISIBLE
            val attachmentsList = ArrayList<JobAttachmentsBean>()
            for (item in response.attachment) {
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

    override fun onProposalsDetailFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null)
            setEmptyView(R.drawable.ic_no_data, loginParams.msg)
    }

    override fun onWithdrawProposalsSuccessfully(response: CommonMessageBean) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onWithdrawProposalsFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
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


}