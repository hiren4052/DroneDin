package com.grewon.dronedin.pilotactivejobs.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface PilotActiveJobsDetailsContract {

    interface View : BaseContract.View {

        fun onJobDetailSuccessfully(response: ActiveJobsDetails)

        fun onJobDetailFailed(loginParams: CommonMessageBean)


        fun showOnScreenProgress()

        fun hideOnScreenProgress()

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getJobsDetails(offersId: String, jobType: String)


    }

}