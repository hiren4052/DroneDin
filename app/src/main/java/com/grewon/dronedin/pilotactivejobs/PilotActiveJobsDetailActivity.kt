package com.grewon.dronedin.pilotactivejobs

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
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
import com.grewon.dronedin.milestone.adapter.ActiveMileStoneAdapter
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.clientprofile.ClientProfileActivity
import com.grewon.dronedin.enum.JOB_REQUEST_STATUS
import com.grewon.dronedin.enum.JOB_REQUEST_TYPE
import com.grewon.dronedin.enum.MILESTONE_REQUEST_STATUS
import com.grewon.dronedin.enum.MILESTONE_REQUEST_TYPE
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.milestone.*
import com.grewon.dronedin.milestone.milestoneadd.MilestoneAddActivity
import com.grewon.dronedin.milestone.milestonecancel.MilestoneCancelRequestActivity
import com.grewon.dronedin.milestone.milestonesubmit.SubmitMilestoneActivity
import com.grewon.dronedin.pilotactivejobs.contract.PilotActiveJobsDetailsContract
import com.grewon.dronedin.project.cancelproject.CancelProjectActivity
import com.grewon.dronedin.project.cancelproject.ProjectCancelRequestActivity
import com.grewon.dronedin.project.endproject.EndProjectActivity
import com.grewon.dronedin.project.endproject.ProjectEndRequestActivity
import com.grewon.dronedin.server.*
import com.grewon.dronedin.utils.TextUtils
import com.grewon.dronedin.utils.TimeUtils

import kotlinx.android.synthetic.main.activity_pilot_active_jobs_detail.*


import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Retrofit
import javax.inject.Inject


class PilotActiveJobsDetailActivity : BaseActivity(), View.OnClickListener,
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
        setContentView(R.layout.activity_pilot_active_jobs_detail)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        img_toolbar.setOnClickListener(this)
        txt_pilot_name.setOnClickListener(this)
        view_cancel_milestone_request.setOnClickListener(this)
        view_end_project_request.setOnClickListener(this)
        view_cancel_project_request.setOnClickListener(this)
    }

    private fun initView() {

        offerId = intent.getStringExtra(AppConstant.ID).toString()


        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        activeJobsDetailsPresenter.attachView(this)
        activeJobsDetailsPresenter.attachApiInterface(retrofit)

        activeJobsDetailsPresenter.getJobsDetails(offerId, AppConstant.ACTIVE_JOB_STATUS)

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
            R.id.img_toolbar -> {
                if (activeJobsDetails != null) {
                    openPopUpMenu()
                }
            }
            R.id.txt_pilot_name -> {
                startActivity(
                    Intent(this, ClientProfileActivity::class.java).putExtra(
                        AppConstant.ID,
                        activeJobsDetails?.userId
                    )
                )
            }
            R.id.view_cancel_milestone_request -> {
                startActivityForResult(
                    Intent(
                        this,
                        MilestoneCancelRequestActivity::class.java
                    ).putExtra(
                        AppConstant.ID,
                        activeJobsDetails?.cancelCompleteRequestMilestone?.milestoneRequestId
                    ), 12
                )
            }
            R.id.view_cancel_project_request -> {
                startActivityForResult(
                    Intent(
                        this,
                        ProjectCancelRequestActivity::class.java
                    ).putExtra(
                        AppConstant.ID,
                        activeJobsDetails?.cancelJobRequest?.jobCancelEndRequestId
                    ), 12
                )
            }
            R.id.view_end_project_request -> {
                startActivityForResult(
                    Intent(
                        this,
                        ProjectEndRequestActivity::class.java
                    ).putExtra(
                        AppConstant.ID,
                        activeJobsDetails?.cancelJobRequest?.jobCancelEndRequestId
                    ).putExtra(AppConstant.JOB_ID,activeJobsDetails?.jobId), 12
                )
            }
        }
    }

    override fun onMilestoneItemClick(jobsDataBean: MilestonesDataBean?) {
        if (jobsDataBean?.milestoneStatus == "active") {
            startActivityForResult(
                Intent(this, SubmitMilestoneActivity::class.java).putExtra(
                    AppConstant.ID,
                    jobsDataBean.milestoneId
                ).putExtra(AppConstant.JOB_ID, activeJobsDetails?.jobId), 12
            )
        } else {
            startActivity(
                Intent(this, MilestoneDetailActivity::class.java).putExtra(
                    AppConstant.ID,
                    jobsDataBean?.milestoneId
                ).putExtra(AppConstant.JOB_ID, activeJobsDetails?.jobId)
            )
        }
    }

    override fun onJobDetailSuccessfully(response: ActiveJobsDetails) {
        if (response != null) {
            setView(response)
        }
    }

    private fun setView(response: ActiveJobsDetails) {
        TextUtils.setTextViewUnderLine(txt_pilot_name)
        activeJobsDetails = response

        txt_subtitle.text = response.category?.categoryName
        txt_job_title.text = response.jobTitle
        txt_job_description.text = response.jobDescription
        txt_job_location.text = response.jobAddress
        txt_pilot_name.text = response.userName
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

        if (response.cancelCompleteRequestMilestone != null && response.cancelCompleteRequestMilestone.milestoneRequestType == MILESTONE_REQUEST_TYPE.complete.name) {

            if (preferenceUtils.getLoginCredentials()?.data?.userId == response.cancelCompleteRequestMilestone.senderId) {

                if (response.cancelCompleteRequestMilestone.milestoneRequestStatus == MILESTONE_REQUEST_STATUS.reject.name) {
                    complete_milestone_request_layout.background =
                        ContextCompat.getDrawable(this, R.color.red)
                    txt_complete_milestone_reason.visibility = View.VISIBLE
                    txt_complete_milestone_reason.text = getString(
                        R.string.reject_reason,
                        response.cancelCompleteRequestMilestone.milestoneRequestRejectReason.toString()
                    )
                    complete_milestone_request_layout.visibility = View.VISIBLE
                } else if (response.cancelCompleteRequestMilestone.milestoneRequestStatus == MILESTONE_REQUEST_STATUS.pending.name) {
                    complete_milestone_request_layout.visibility = View.GONE
                } else if (response.cancelCompleteRequestMilestone.milestoneRequestStatus == MILESTONE_REQUEST_STATUS.accept.name) {
                    complete_milestone_request_layout.visibility = View.VISIBLE
                    complete_milestone_request_layout.background =
                        ContextCompat.getDrawable(this, R.color.top_green)
                    txt_complete_milestone_reason.visibility = View.GONE
                }

                view_complete_milestone_request.visibility = View.GONE

                activeJobsDetailsPresenter.readSentRequest(response.jobId.toString())

            } else {
                if (response.cancelCompleteRequestMilestone.milestoneRequestStatus == MILESTONE_REQUEST_STATUS.pending.name) {
                    complete_milestone_request_layout.visibility = View.VISIBLE
                    txt_complete_milestone_reason.visibility = View.GONE
                } else {
                    complete_milestone_request_layout.visibility = View.GONE
                }
            }

            txt_complete_milestone.text = response.cancelCompleteRequestMilestone.msg

        } else {

            complete_milestone_request_layout.visibility = View.GONE

        }



        if (response.cancelCompleteRequestMilestone != null && response.cancelCompleteRequestMilestone.milestoneRequestType == MILESTONE_REQUEST_TYPE.cancel.name) {
            if (preferenceUtils.getLoginCredentials()?.data?.userId == response.cancelCompleteRequestMilestone.senderId) {
                if (response.cancelCompleteRequestMilestone.milestoneRequestStatus == MILESTONE_REQUEST_STATUS.reject.name) {
                    cancel_milestone_request_layout.background =
                        ContextCompat.getDrawable(this, R.color.red)
                    txt_cancel_milestone_reason.visibility = View.VISIBLE
                    txt_cancel_milestone_reason.text = getString(
                        R.string.reject_reason,
                        response.cancelCompleteRequestMilestone.milestoneRequestRejectReason.toString()
                    )
                    cancel_milestone_request_layout.visibility = View.VISIBLE
                } else if (response.cancelCompleteRequestMilestone.milestoneRequestStatus == MILESTONE_REQUEST_STATUS.pending.name) {
                    cancel_milestone_request_layout.visibility = View.GONE
                } else if (response.cancelCompleteRequestMilestone.milestoneRequestStatus == MILESTONE_REQUEST_STATUS.accept.name) {
                    cancel_milestone_request_layout.background =
                        ContextCompat.getDrawable(this, R.color.top_green)
                    txt_cancel_milestone_reason.visibility = View.GONE
                    cancel_milestone_request_layout.visibility = View.VISIBLE
                }

                view_cancel_milestone_request.visibility = View.GONE

                activeJobsDetailsPresenter.readSentRequest(response.jobId.toString())
            } else {
                if (response.cancelCompleteRequestMilestone.milestoneRequestStatus == MILESTONE_REQUEST_STATUS.pending.name) {
                    cancel_milestone_request_layout.visibility = View.VISIBLE
                    txt_cancel_milestone_reason.visibility = View.GONE
                } else if (response.cancelCompleteRequestMilestone.milestoneRequestStatus == MILESTONE_REQUEST_STATUS.accept.name) {
                    cancel_milestone_request_layout.visibility = View.GONE
                }
            }

            txt_cancel_milestone.text = response.cancelCompleteRequestMilestone.msg

        } else {

            cancel_milestone_request_layout.visibility = View.GONE

        }

        if (response.requestedMilestone != null) {
            if (response.requestedMilestone.milestoneRequestStatus == MILESTONE_REQUEST_STATUS.reject.name) {
                add_milestone_request_layout.background =
                    ContextCompat.getDrawable(this, R.color.red)
                txt_add_milestone_reason.text = getString(
                    R.string.reject_reason,
                    response.requestedMilestone.milestoneRequestRejectReason
                )
            } else {
                add_milestone_request_layout.background =
                    ContextCompat.getDrawable(this, R.color.top_green)
                txt_add_milestone_reason.visibility = View.GONE
            }

            add_milestone_request_layout.visibility = View.VISIBLE
            txt_add_milestone.text = response.requestedMilestone.msg

            activeJobsDetailsPresenter.readSentRequest(response.jobId.toString())

        } else {

            add_milestone_request_layout.visibility = View.GONE

        }


        if (response.cancelJobRequest != null && response.cancelJobRequest.requestType == JOB_REQUEST_TYPE.cancel.name) {
            if (preferenceUtils.getLoginCredentials()?.data?.userId == response.cancelJobRequest.senderId) {
                when (response.cancelJobRequest.requestStatus) {
                    JOB_REQUEST_STATUS.rejected.name -> {
                        cancel_project_request_layout.background =
                            ContextCompat.getDrawable(this, R.color.red)
                        txt_cancel_project_reason.visibility = View.VISIBLE
                        txt_cancel_project_reason.text = getString(
                            R.string.reject_reason,
                            response.cancelJobRequest.requestRejectReason.toString()
                        )
                        cancel_project_request_layout.visibility = View.VISIBLE
                    }
                    JOB_REQUEST_STATUS.pending.name -> {
                        cancel_project_request_layout.visibility = View.GONE
                    }
                    JOB_REQUEST_STATUS.accepted.name -> {
                        cancel_project_request_layout.background =
                            ContextCompat.getDrawable(this, R.color.top_green)
                        txt_cancel_project_reason.visibility = View.GONE
                        cancel_project_request_layout.visibility = View.VISIBLE
                    }
                }

                view_cancel_project_request.visibility = View.GONE

                activeJobsDetailsPresenter.readSentRequest(response.jobId.toString())
            } else {
                if (response.cancelJobRequest.requestStatus == MILESTONE_REQUEST_STATUS.pending.name) {
                    cancel_project_request_layout.visibility = View.VISIBLE
                    txt_cancel_project_reason.visibility = View.GONE
                } else if (response.cancelJobRequest.requestStatus == MILESTONE_REQUEST_STATUS.accept.name) {
                    cancel_project_request_layout.visibility = View.GONE
                }
            }
            txt_cancel_project.text = response.cancelJobRequest.msg
        } else {
            cancel_project_request_layout.visibility = View.GONE
        }


        if (response.cancelJobRequest != null && response.cancelJobRequest.requestType == JOB_REQUEST_TYPE.end.name) {
            if (preferenceUtils.getLoginCredentials()?.data?.userId == response.cancelJobRequest.senderId) {
                when (response.cancelJobRequest.requestStatus) {
                    JOB_REQUEST_STATUS.rejected.name -> {
                        end_project_request_layout.background =
                            ContextCompat.getDrawable(this, R.color.red)

                        end_project_request_layout.visibility = View.VISIBLE
                    }
                    JOB_REQUEST_STATUS.pending.name -> {
                        end_project_request_layout.visibility = View.GONE
                    }
                    JOB_REQUEST_STATUS.accepted.name -> {
                        end_project_request_layout.background =
                            ContextCompat.getDrawable(this, R.color.top_green)
                        end_project_request_layout.visibility = View.VISIBLE
                    }
                }

                view_cancel_project_request.visibility = View.GONE

                activeJobsDetailsPresenter.readSentRequest(response.jobId.toString())
            } else {
                if (response.cancelJobRequest.requestStatus == MILESTONE_REQUEST_STATUS.pending.name) {
                    end_project_request_layout.visibility = View.VISIBLE
                } else if (response.cancelJobRequest.requestStatus == MILESTONE_REQUEST_STATUS.accept.name) {
                    end_project_request_layout.visibility = View.GONE
                }
            }
            txt_end_project.text = response.cancelJobRequest.msg
        } else {
            end_project_request_layout.visibility = View.GONE
        }


    }

    private fun openPopUpMenu() {
        val popup = PopupMenu(this, img_toolbar)
        //Inflating the Popup using xml file
        //Inflating the Popup using xml file
        popup.menuInflater.inflate(R.menu.pilot_active_job_toolbar, popup.menu)

        //registering popup with OnMenuItemClickListener

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.im_add_milestone -> {
                    startActivityForResult(
                        Intent(
                            this,
                            MilestoneAddActivity::class.java
                        ).putExtra(AppConstant.ID, activeJobsDetails?.jobId), 12
                    )
                }
                R.id.im_cancel_project -> {
                    startActivityForResult(
                        Intent(this, CancelProjectActivity::class.java).putExtra(
                            AppConstant.ID,
                            activeJobsDetails?.jobId
                        ), 12
                    )
                }
                R.id.im_end_project -> {
                    startActivityForResult(
                        Intent(this, EndProjectActivity::class.java).putExtra(
                            AppConstant.ID,
                            activeJobsDetails?.jobId
                        ), 12
                    )
                }
                R.id.im_message -> {
                    startActivity(
                        Intent(this, ChatActivity::class.java).putExtra(
                            AppConstant.ID,
                            activeJobsDetails?.userId
                        )
                    )
                }
            }
            true
        }

        popup.show()
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