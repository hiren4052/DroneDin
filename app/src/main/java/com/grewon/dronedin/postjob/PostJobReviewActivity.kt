package com.grewon.dronedin.postjob

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.extraadapter.ChipEquipmentsAdapter
import com.grewon.dronedin.extraadapter.ChipSkillsAdapter
import com.grewon.dronedin.milestone.adapter.MileStoneAdapter
import com.grewon.dronedin.postjob.contract.JobPostContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.CreateJobsBean
import com.grewon.dronedin.server.JobAttachmentsBean
import com.grewon.dronedin.server.MilestonesDataBean
import com.grewon.dronedin.server.params.CreateJobsParams
import kotlinx.android.synthetic.main.activity_post_job_review.*
import kotlinx.android.synthetic.main.activity_post_job_review.chip_equipments
import kotlinx.android.synthetic.main.activity_post_job_review.chip_skills
import kotlinx.android.synthetic.main.activity_post_job_review.image_recycle
import kotlinx.android.synthetic.main.activity_post_job_review.mile_stone_recycle
import kotlinx.android.synthetic.main.activity_post_job_review.milestone_layout
import kotlinx.android.synthetic.main.activity_post_job_review.pictures_layout
import kotlinx.android.synthetic.main.activity_post_job_review.txt_budget
import kotlinx.android.synthetic.main.activity_post_job_review.txt_edit
import kotlinx.android.synthetic.main.activity_post_job_review.txt_job_description
import kotlinx.android.synthetic.main.activity_post_job_review.txt_job_location
import kotlinx.android.synthetic.main.activity_post_job_review.txt_job_title
import kotlinx.android.synthetic.main.activity_posted_job_details.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.img_back
import retrofit2.Retrofit
import javax.inject.Inject

class PostJobReviewActivity : BaseActivity(), View.OnClickListener, JobPostContract.View {

    private var submitParams: CreateJobsParams? = null

    @Inject
    lateinit var postJobPresenter: JobPostContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var mileStoneAdapter: MileStoneAdapter? = null
    private var jobsImageAdapter: JobAttachmentsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job_review)
        setClicks()
        initView()
    }

    private fun initView() {

        submitParams = intent.getParcelableExtra(AppConstant.BEAN)


        txt_toolbar_title.text = getString(R.string.review_job)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)


        postJobPresenter.attachView(this)
        postJobPresenter.attachApiInterface(retrofit)

        setView()

    }

    private fun setView() {


        txt_job_title.text = submitParams?.jobTitle
        txt_job_description.text = submitParams?.jobDescription
        txt_job_location.text = submitParams?.jobAddress
        txt_job_category.text =
            submitParams?.categoryList?.filter { it.isSelected == 1 }?.map { it.categoryName }
                ?.joinToString(",")

        txt_budget.text = getString(R.string.price_string, submitParams?.jobTotalPrice)



        if (submitParams?.mileStones != null && submitParams?.mileStones!!.size > 0) {
            milestone_layout.visibility = View.VISIBLE
            val milestoneList = ArrayList<MilestonesDataBean>()
            for (item in submitParams?.mileStones!!) {
                val milesStone = MilestonesDataBean()
                milesStone.milestoneDetails = item.details
                milesStone.milestonePrice = item.price
                milesStone.milestoneId = item.milestoneId
                milestoneList.add(milesStone)
            }
            setMileStoneAdapter(milestoneList)
        } else {
            milestone_layout.visibility = View.GONE
        }

        if (submitParams?.skillList != null && submitParams?.skillList?.size!! > 0) {
            val skillsList =
                submitParams?.skillList?.filter { it.isSelected == 1 }?.map { it.skill.toString() }
            setSkillsAdapter(skillsList!!)
        }

        if (submitParams?.equipmentsList != null && submitParams?.equipmentsList!!.size > 0) {
            val skillsList = submitParams?.equipmentsList?.filter { it.isSelected == 1 }
                ?.map { it.equipment.toString() }
            setEquipmentsData(skillsList!!)
        }


        if (submitParams?.attachments != null && submitParams?.attachments!!.size > 0) {
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


    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_edit.setOnClickListener(this)
        txt_post.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_edit, R.id.img_back -> {
                finish()
            }

            R.id.txt_post -> {
                postJobPresenter.postJob(submitParams!!)
            }
        }
    }

    override fun onPostJobSuccessFully(loginParams: CreateJobsBean) {
        if (loginParams.id != null) {
            startActivityForResult(
                Intent(
                    this,
                    PostJobSubmittedActivity::class.java
                ).putExtra(AppConstant.ID, loginParams.id),
                77
            )
        }


    }

    override fun onPostJobFailed(loginParams: CreateJobsParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onDeleteMilestoneSuccessfully(commonMessageBean: CommonMessageBean) {

    }

    override fun onDeleteMilestoneFailed(commonMessageBean: CommonMessageBean) {

    }

    override fun onDeleteAttachmentSuccessfully(commonMessageBean: CommonMessageBean) {

    }

    override fun onDeleteAttachmentFailed(commonMessageBean: CommonMessageBean) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 77 && resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        }
    }

}