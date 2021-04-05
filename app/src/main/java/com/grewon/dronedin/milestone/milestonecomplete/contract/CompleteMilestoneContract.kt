package com.grewon.dronedin.milestone.milestonecomplete.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface CompleteMilestoneContract {

    interface View : BaseContract.View {


        fun onCompleteMilestoneGetSuccessFully(loginParams: CompleteMilestoneRequest)

        fun onCompleteMilestoneGetFailed(loginParams: CommonMessageBean)

        fun onApiException(error: Int)

        fun onCompleteMilestoneStatusSuccessFully(loginParams: CommonMessageBean)

        fun onCompleteMilestoneStatusFailed(loginParams: CommonMessageBean)


    }

    interface Presenter : BaseContract.Presenter<View> {


        fun getCompleteMilestoneDetail(milestoneRequestId: String)

        fun milestoneStatusUpdate(params: CompleteMilestoneStatusUpdateParams)
    }

}