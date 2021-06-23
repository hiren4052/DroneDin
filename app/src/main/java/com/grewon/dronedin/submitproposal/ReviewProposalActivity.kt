package com.grewon.dronedin.submitproposal

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
import com.grewon.dronedin.milestone.adapter.MileStoneAdapter
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.JobAttachmentsBean
import com.grewon.dronedin.server.MilestonesDataBean
import com.grewon.dronedin.server.params.SubmitProposalParams
import com.grewon.dronedin.submitproposal.contract.SubmitProposalContract
import kotlinx.android.synthetic.main.activity_review_proposal.*
import kotlinx.android.synthetic.main.activity_review_proposal.milestone_layout
import kotlinx.android.synthetic.main.activity_review_proposal.pictures_layout
import kotlinx.android.synthetic.main.activity_review_proposal.txt_budget
import kotlinx.android.synthetic.main.activity_review_proposal.txt_job_description
import kotlinx.android.synthetic.main.activity_review_proposal.txt_job_title
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.img_back
import retrofit2.Retrofit
import javax.inject.Inject

class ReviewProposalActivity : BaseActivity(), View.OnClickListener, SubmitProposalContract.View {

    @Inject
    lateinit var submitProposalPresenter: SubmitProposalContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var submitParams: SubmitProposalParams? = null

    private var mileStoneAdapter: MileStoneAdapter? = null
    private var jobsImageAdapter: JobAttachmentsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_proposal)
        setClicks()
        initView()
    }

    private fun initView() {
        submitParams = intent.getParcelableExtra(AppConstant.BEAN)


        txt_toolbar_title.text = getString(R.string.review_proposal)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        submitProposalPresenter.attachView(this)
        submitProposalPresenter.attachApiInterface(retrofit)


        setView()
    }

    private fun setView() {

        txt_job_title.text = submitParams?.proposal_title
        txt_job_description.text = submitParams?.proposal_description
        txt_budget.text = getString(R.string.price_string, submitParams?.proposal_total_price)




        if (submitParams?.milestone != null && submitParams?.milestone!!.size > 0) {
            milestone_layout.visibility = View.VISIBLE
            val milestoneList = ArrayList<MilestonesDataBean>()
            for (item in submitParams?.milestone!!) {
                val milesStone = MilestonesDataBean()
                milesStone.milestoneDetails = item?.details
                milesStone.milestonePrice = item?.price
                milesStone.milestoneId = item?.milestoneId
                milestoneList.add(milesStone)
            }
            setMileStoneAdapter(milestoneList)
        } else {
            milestone_layout.visibility = View.GONE
        }

        if (submitParams?.attachments != null && submitParams?.attachments?.size!! > 0) {
            pictures_layout.visibility = View.VISIBLE
            val attachmentsList = ArrayList<JobAttachmentsBean>()
            for (item in submitParams?.attachments!!) {
                val attachments = JobAttachmentsBean()
                attachments.attachment = item.filePath
                attachments.attachmentId = item.attachmentId
                attachmentsList.add(attachments)
            }
            setAttachmentsAdapter(attachmentsList)

        } else {
            pictures_layout.visibility = View.GONE
        }

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

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_edit.setOnClickListener(this)
        txt_submit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_edit, R.id.img_back -> {
                finish()
            }

            R.id.txt_submit -> {
                DroneDinApp.loadingDialogMessage=getString(R.string.submitting)
                submitProposalPresenter.submitProposal(submitParams!!)
            }
        }
    }

    override fun onSubmitProposalSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onSubmitProposalFailed(loginParams: SubmitProposalParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

}