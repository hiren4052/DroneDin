package com.grewon.dronedin.pilotmyjobs.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface PilotFindJobsDetailContract {

    interface View : BaseContract.View {

        fun onJobsDetailSuccessfully(response: PilotFindJobsDetailBean)

        fun onJobsDetailFailed(loginParams: CommonMessageBean)

        fun showOnScreenProgress()

        fun hideOnScreenProgress()

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getPilotJobDetails(jobId: String, jobType: String)

    }

}