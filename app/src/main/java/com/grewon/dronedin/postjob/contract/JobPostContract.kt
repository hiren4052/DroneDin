package com.grewon.dronedin.postjob.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface JobPostContract {

    interface View : BaseContract.View {


        fun onPostJobSuccessFully(loginParams: CreateJobsBean)

        fun onPostJobFailed(loginParams: CreateJobsParams)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {


        fun postJob(params: CreateJobsParams)


    }

}