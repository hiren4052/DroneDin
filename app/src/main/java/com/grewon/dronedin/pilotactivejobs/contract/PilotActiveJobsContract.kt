package com.grewon.dronedin.pilotactivejobs.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface PilotActiveJobsContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onJobsDataGetSuccessful(response: PilotJobsDataBean)

        fun onJobsDataGetFailed(loginParams: CommonMessageBean)

        fun onJobsSaveSuccessful(response: CommonMessageBean)

        fun onJobsSaveFailed(loginParams: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getPilotJobs(filterParams: FilterParams)

        fun saveJobs(jobId: String)


    }

}