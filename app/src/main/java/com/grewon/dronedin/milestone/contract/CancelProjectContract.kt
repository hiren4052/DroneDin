package com.grewon.dronedin.milestone.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface CancelProjectContract {

    interface View : BaseContract.View {


        fun onCancelSuccessFully(loginParams: CommonMessageBean)

        fun onCancelFailed(loginParams: CancelMilestoneParams)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {


        fun cancelProject(jobId: String, requestType: String)


    }

}