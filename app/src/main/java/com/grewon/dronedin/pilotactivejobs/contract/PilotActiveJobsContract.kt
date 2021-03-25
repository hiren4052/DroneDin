package com.grewon.dronedin.pilotactivejobs.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface PilotActiveJobsContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onJobsDataGetSuccessful(response: PilotActiveJobsData)

        fun onJobsDataGetFailed(loginParams: CommonMessageBean)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getPilotActiveJobs(getJobsParams: GetJobsParams)



    }

}