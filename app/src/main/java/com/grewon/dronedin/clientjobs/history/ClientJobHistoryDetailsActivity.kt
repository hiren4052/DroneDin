package com.grewon.dronedin.clientjobs.history

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
import com.grewon.dronedin.milestone.adapter.ActiveMileStoneAdapter
import com.grewon.dronedin.milestone.MilestoneDetailActivity
import com.grewon.dronedin.milestone.milestonesubmit.SubmitMilestoneActivity
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.pilotactivejobs.contract.PilotActiveJobsDetailsContract
import com.grewon.dronedin.pilotprofile.PilotProfileActivity
import com.grewon.dronedin.review.SubmitReviewActivity
import com.grewon.dronedin.server.ActiveJobsDetails
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.JobAttachmentsBean
import com.grewon.dronedin.server.MilestonesDataBean
import com.grewon.dronedin.utils.TextUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_client_job_history_details.*
import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Retrofit
import javax.inject.Inject


class ClientJobHistoryDetailsActivity : BaseActivity(), View.OnClickListener,
    ActiveMileStoneAdapter.OnItemClickListeners, PilotActiveJobsDetailsContract.View {


    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var activeJobsDetailsPresenter: PilotActiveJobsDetailsContract.Presenter

    private var mileStoneAdapter: ActiveMileStoneAdapter? = null
    private var jobsImageAdapter: JobAttachmentsAdapter? = null

    private var activeJobsDetails: ActiveJobsDetails? = null

    private var offerId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_job_history_details)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_submit_review.setOnClickListener(this)
        txt_message.setOnClickListener(this)
        txt_pilot_name.setOnClickListener(this)
    }

    private fun initView() {

        offerId = intent.getStringExtra(AppConstant.ID).toString()


        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        activeJobsDetailsPresenter.attachView(this)
        activeJobsDetailsPresenter.attachApiInterface(retrofit)

        activeJobsDetailsPresenter.getJobsDetails(offerId, "offer")

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
        mileStoneAdapter = ActiveMileStoneAdapter(this, this)
        mile_stone_recycle.adapter = mileStoneAdapter
        mileStoneAdapter?.addItemsList(milestoneList)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_submit_review -> {
                startActivity(
                    Intent(
                        this,
                        SubmitReviewActivity::class.java
                    ).putExtra(AppConstant.ID, activeJobsDetails?.jobId)
                )
            }
            R.id.txt_pilot_name -> {
                startActivity(
                    Intent(
                        this,
                        PilotProfileActivity::class.java
                    ).putExtra(AppConstant.ID, activeJobsDetails?.pilot?.userId)
                )
            }
            R.id.txt_message -> {
                startActivity(
                    Intent(this, ChatActivity::class.java).putExtra(
                        AppConstant.ID,
                        activeJobsDetails?.pilot?.userId
                    )
                )
            }
        }
    }

    override fun onMilestoneItemClick(jobsDataBean: MilestonesDataBean?) {
        if (jobsDataBean?.milestoneStatus == "active") {
            startActivity(Intent(this, SubmitMilestoneActivity::class.java))
        } else {
            startActivity(
                Intent(this, MilestoneDetailActivity::class.java).putExtra(
                    AppConstant.ID,
                    jobsDataBean?.milestoneId
                )
            )
        }
    }


    private fun setView(response: ActiveJobsDetails) {

        activeJobsDetails = response

        TextUtils.setTextViewUnderLine(txt_pilot_name)

        txt_subtitle.text = response.category?.categoryName
        txt_job_title.text = response.offerTitle
        txt_job_description.text = response.offerDescription
        txt_job_location.text = response.jobAddress
        txt_pilot_name.text = response.pilot?.userName
        txt_budget.text = getString(R.string.price_string, response.offerTotalPrice)
        txt_job_date.text = TimeUtils.getServerToAppDate(response.date.toString())



        if (response.milestone != null && response.milestone.size > 0) {
            milestone_layout.visibility = View.VISIBLE
            val milestoneList = ArrayList<MilestonesDataBean>()
            for (item in response.milestone) {
                val milesStone = MilestonesDataBean()
                milesStone.milestoneDetails = item.milestoneDetails
                milesStone.milestonePrice = item.milestonePrice
                milesStone.milestoneId = item.milestoneId
                milesStone.milestoneRequestId = item.milestoneRequestId
                milesStone.milestoneStatus = item.milestoneStatus
                milesStone.milestoneCompletedDate = item.milestoneCompletedDate
                milesStone.milestoneStartedDate = item.milestoneStartedDate
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

    override fun onJobDetailSuccessfully(response: ActiveJobsDetails) {
        if (response != null) {
            setView(response)
        }
    }


    override fun onJobDetailFailed(loginParams: CommonMessageBean) {
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 12) {
                activeJobsDetailsPresenter.getJobsDetails(offerId, AppConstant.ACTIVE_JOB_STATUS)
            }
        }
    }
}