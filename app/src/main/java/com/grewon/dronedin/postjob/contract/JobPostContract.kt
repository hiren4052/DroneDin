package com.grewon.dronedin.postjob.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface JobPostContract {

    interface View : BaseContract.View {


        fun onPostJobSuccessFully(loginParams: CreateJobsBean)

        fun onPostJobFailed(loginParams: CreateJobsParams)

        fun onApiException(error: Int)


        fun onDeleteMilestoneSuccessfully(commonMessageBean: CommonMessageBean)

        fun onDeleteMilestoneFailed(commonMessageBean: CommonMessageBean)

        fun onDeleteAttachmentSuccessfully(commonMessageBean: CommonMessageBean)

        fun onDeleteAttachmentFailed(commonMessageBean: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {


        fun postJob(params: CreateJobsParams)

        fun editJob(jobId: String, params: CreateJobsParams)

        fun deleteAttachment(attachmentId: String)

        fun deleteMilestone(milestoneId: String)

    }

}