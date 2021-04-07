package com.grewon.dronedin.clientjobs.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface ClientJobsDetailContract {

    interface View : BaseContract.View {

        fun onPostedJobsDetailSuccessfully(response: PostedJobDetailBean)

        fun onPostedJobsDetailFailed(loginParams: CommonMessageBean)

        fun showOnScreenProgress()

        fun hideOnScreenProgress()

        fun onApiException(error: Int)

        fun onJobDeletedSuccessfully(response: CommonMessageBean)

        fun onJobDeletedFailed(loginParams: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getClientJobDetails(jobId: String, jobType: String)

        fun deleteJob(jobId: String)


    }

}