package com.grewon.dronedin.filter.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface FilterContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onPilotDataGetSuccessful(response: PilotDataBean)

        fun onPilotDataGetFailed(loginParams: CommonMessageBean)

        fun onPilotSaveSuccessful(response: CommonMessageBean)

        fun onPilotSaveFailed(loginParams: CommonMessageBean)

        fun onJobsDataGetSuccessful(response: PilotJobsDataBean)

        fun onJobsDataGetFailed(loginParams: CommonMessageBean)

        fun onJobsSaveSuccessful(response: CommonMessageBean)

        fun onJobsSaveFailed(loginParams: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getPilotData(filterParams: FilterParams)

        fun savePilots(pilotId: String)

        fun getPilotJobs(filterParams: FilterParams)

        fun saveJobs(jobId: String)

    }

}