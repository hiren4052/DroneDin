package com.grewon.dronedin.clientjobs.posted

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.clientjobs.clientoffers.ClientOffersActivity
import com.grewon.dronedin.clientjobs.contract.ClientJobsDetailContract
import com.grewon.dronedin.extraadapter.ChipEquipmentsAdapter
import com.grewon.dronedin.extraadapter.ChipSkillsAdapter
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.milestone.adapter.MileStoneAdapter
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.JobAttachmentsBean
import com.grewon.dronedin.server.MilestonesDataBean
import com.grewon.dronedin.server.PostedJobDetailBean
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_posted_job_details.*
import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Retrofit
import javax.inject.Inject


class PostedJobDetailsActivity : BaseActivity(), View.OnClickListener,
    ClientJobsDetailContract.View {

    @Inject
    lateinit var clientJobsDetailPresenter: ClientJobsDetailContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var mileStoneAdapter: MileStoneAdapter? = null
    private var jobsImageAdapter: JobAttachmentsAdapter? = null

    private var jobId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posted_job_details)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_edit.setOnClickListener(this)
        txt_delete.setOnClickListener(this)
        txt_see_list.setOnClickListener(this)
        img_toolbar.setOnClickListener(this)
    }


    private fun initView() {

        jobId = intent.getStringExtra(AppConstant.ID).toString()

        DroneDinApp.getAppInstance().getAppComponent().inject(this)

        clientJobsDetailPresenter.attachView(this)
        clientJobsDetailPresenter.attachApiInterface(retrofit)


        clientJobsDetailPresenter.getClientJobDetails(jobId, AppConstant.POSTED_JOB_STATUS)

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
            R.id.txt_edit -> {
                finish()
            }
            R.id.txt_delete -> {
                DroneDinApp.getAppInstance().setDialogMessage(getString(R.string.please_wait))
                clientJobsDetailPresenter.deleteJob(jobId)
            }
            R.id.txt_see_list -> {
                startActivity(Intent(this, ClientProposalsActivity::class.java))
            }
            R.id.img_toolbar -> {
                openPopUpMenu()
            }
        }
    }

    private fun openPopUpMenu() {
        val popup = PopupMenu(this, img_toolbar)
        //Inflating the Popup using xml file
        //Inflating the Popup using xml file
        popup.menuInflater.inflate(R.menu.posted_job_bottom, popup.menu)

        //registering popup with OnMenuItemClickListener

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener {
            if (it.itemId == R.id.im_saved_pilots) {

            } else if (it.itemId == R.id.im_sent_invitations) {

            } else if (it.itemId == R.id.im_sent_offers) {
                startActivity(Intent(this, ClientOffersActivity::class.java))
            }
            true
        }

        popup.show()
    }

    override fun onPostedJobsDetailSuccessfully(response: PostedJobDetailBean) {
        if (response != null) {
            setView(response)
        }
    }

    private fun setView(response: PostedJobDetailBean) {


        txt_received_proposal.text = response.totalProposal
        txt_subtitle.text = response.category?.categoryName
        txt_job_title.text = response.jobTitle
        txt_job_description.text = response.jobDescription
        txt_job_location.text = response.jobAddress
        txt_budget.text = getString(R.string.price_string, response.totalPrice)
        txt_job_date.text = TimeUtils.getServerToAppDate(response.date.toString())


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

    override fun onPostedJobsDetailFailed(loginParams: CommonMessageBean) {
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

    override fun onJobDeletedSuccessfully(response: CommonMessageBean) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onJobDeletedFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null)
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
    }


    private fun setEmptyView(drawableId: Int, errorMessage: String) {
        no_data_layout.visibility = View.VISIBLE
        txt_no_data_image.setImageResource(drawableId)
        txt_no_data_title.text = errorMessage
    }
}