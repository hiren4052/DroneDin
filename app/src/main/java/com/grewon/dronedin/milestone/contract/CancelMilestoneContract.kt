package com.grewon.dronedin.milestone.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface CancelMilestoneContract {

    interface View : BaseContract.View {


        fun onCancelSuccessFully(loginParams: CommonMessageBean)

        fun onCancelFailed(loginParams: CancelMilestoneParams)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {


        fun cancelMilestone(params: CancelMilestoneParams)


    }

}