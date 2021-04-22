package com.grewon.dronedin.pilotfindjobs

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.extraadapter.ChipEquipmentsAdapter
import com.grewon.dronedin.extraadapter.ChipSkillsAdapter
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.clientprofile.ClientProfileActivity
import com.grewon.dronedin.milestone.adapter.MileStoneAdapter
import com.grewon.dronedin.pilotmyjobs.contract.PilotFindJobsDetailContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.submitproposal.SubmitProposalActivity
import com.grewon.dronedin.utils.TextUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_find_jobs_details.*

import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Retrofit
import javax.inject.Inject

class FindJobsDetailsActivity : BaseActivity(), View.OnClickListener,
    PilotFindJobsDetailContract.View {

    private var jobId: String = ""

    @Inject
    lateinit var pilotFindJobsDetailsPresenter: PilotFindJobsDetailContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var mileStoneAdapter: MileStoneAdapter? = null
    private var jobsImageAdapter: JobAttachmentsAdapter? = null

    private var pilotFindJobsBean: PilotFindJobsDetailBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_jobs_details)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_message.setOnClickListener(this)
        txt_send_proposal.setOnClickListener(this)
        txt_client_name.setOnClickListener(this)
    }

    private fun initView() {
        jobId = intent.getStringExtra(AppConstant.ID).toString()

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        pilotFindJobsDetailsPresenter.attachView(this)
        pilotFindJobsDetailsPresenter.attachApiInterface(retrofit)

        pilotFindJobsDetailsPresenter.getPilotJobDetails(jobId, "pilot_job_detail")

    }

    private fun setSkillsAdapter(skillsList: List<String>) {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        chip_skills.layoutManager = layoutManager
        val skillsAdapter = ChipSkillsAdapter(this, R.color.gray_f4, R.color.gray_71)
        chip_skills.adapter = skillsAdapter
        skillsAdapter.addItemsList(skillsList as ArrayList<String>)

    }

    private fun setEquipmentsData(equipmentsList: List<String>) {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        chip_equipments.layoutManager = layoutManager
        val equipmentsAdapter = ChipEquipmentsAdapter(this, R.color.light_sky_blue, R.color.gray_71)
        chip_equipments.adapter = equipmentsAdapter
        equipmentsAdapter.addItemsList(equipmentsList as ArrayList<String>)

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
            R.id.txt_message -> {
                startActivity(
                    Intent(this, ChatActivity::class.java).putExtra(
                        AppConstant.ID,
                        pilotFindJobsBean?.userId
                    )
                )
            }
            R.id.txt_client_name -> {
                startActivity(
                    Intent(this, ClientProfileActivity::class.java).putExtra(
                        AppConstant.ID,
                        pilotFindJobsBean?.userId
                    )
                )
            }
            R.id.txt_send_proposal -> {
                startActivityForResult(
                    Intent(
                        this,
                        SubmitProposalActivity::class.java
                    ).putExtra(AppConstant.ID, jobId)
                        .putExtra(AppConstant.BEAN, mileStoneAdapter?.itemList)
                        .putExtra(AppConstant.PRICE, txt_budget.text.toString().replace("$", ""))
                        .putExtra(AppConstant.TITLE, pilotFindJobsBean?.jobTitle)
                        .putExtra(AppConstant.DESCRIPTION, pilotFindJobsBean?.jobDescription),
                    12
                )
            }
        }
    }

    override fun onJobsDetailSuccessfully(response: PilotFindJobsDetailBean) {
        if (response != null) {
            setView(response)
        }


    }

    private fun setView(response: PilotFindJobsDetailBean) {

        TextUtils.setTextViewUnderLine(txt_client_name)

        pilotFindJobsBean = response

        txt_proposal_received.text = response.totalProposal
        txt_interviewing.text = response.totalInterviewing
        txt_invitations_sent.text = response.totalInvitation
        txt_subtitle.text = response.category?.categoryName
        txt_job_title.text = response.jobTitle
        txt_job_description.text = response.jobDescription
        txt_job_location.text = response.jobAddress
        txt_client_name.text = response.userName
        txt_budget.text = getString(R.string.price_string, response.totalPrice)
        txt_job_date.text = TimeUtils.getServerToAppDate(response.date.toString())


        if (response.proposalId != null) {
            txt_send_proposal.isEnabled = false
            txt_send_proposal.alpha = 0.3f
        } else {
            txt_send_proposal.isEnabled = true
        }

        if (response.milestone != null && response.milestone.size > 0) {
            milestone_layout.visibility = View.VISIBLE
            val milestoneList = ArrayList<MilestonesDataBean>()
            for (item in response.milestone) {
                val milesStone = MilestonesDataBean()
                milesStone.milestoneDetails = item.milestoneDetails
                milesStone.milestonePrice = item.milestonePrice
                milesStone.milestoneId = item.milestoneId
                milestoneList.add(milesStone)
            }
            setMileStoneAdapter(milestoneList)
        } else {
            milestone_layout.visibility = View.GONE
        }

        if (response.skill != null && response.skill.size > 0) {
            val skillsList = response.skill.map { it.skill.toString() }
            setSkillsAdapter(skillsList)
        }

        if (response.equipment != null && response.equipment.size > 0) {
            val skillsList = response.equipment.map { it.equipment.toString() }
            setEquipmentsData(skillsList)
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


    override fun onJobsDetailFailed(loginParams: CommonMessageBean) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 12) {
                pilotFindJobsDetailsPresenter.getPilotJobDetails(jobId, "pilot_job_detail")
            }
        }
    }


    private fun setEmptyView(drawableId: Int, errorMessage: String) {
        no_data_layout.visibility = View.VISIBLE
        txt_no_data_image.setImageResource(drawableId)
        txt_no_data_title.text = errorMessage
    }

}