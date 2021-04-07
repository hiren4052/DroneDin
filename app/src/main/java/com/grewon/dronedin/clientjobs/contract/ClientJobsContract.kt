package com.grewon.dronedin.clientjobs.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface ClientJobsContract {

    interface View : BaseContract.View {



        fun onPostedJobsGetSuccessful(response: JobsDataBean)

        fun onActiveJobsGetSuccessful(response: JobsDataBean)

        fun onJobsHistoryGetSuccessful(response: JobsDataBean)

        fun onJobsGetFailed(loginParams: CommonMessageBean)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getClientJobs(filterParams: GetJobsParams)



    }

}