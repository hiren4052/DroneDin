package com.grewon.dronedin.pilotjobhistory.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface PilotHistoryJobsContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onHistoryDataGetSuccessful(response: PilotJobHistoryBean)

        fun onHistoryDataGetFailed(loginParams: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getPilotHistory(filterParams: GetJobsParams)

    }

}