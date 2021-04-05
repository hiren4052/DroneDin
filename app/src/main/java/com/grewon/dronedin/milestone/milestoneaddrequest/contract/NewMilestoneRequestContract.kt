package com.grewon.dronedin.milestone.milestoneaddrequest.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface NewMilestoneRequestContract {

    interface View : BaseContract.View {


        fun onNewMilestoneGetSuccessFully(loginParams: NewMilestoneRequest)

        fun onNewMilestoneGetFailed(loginParams: CommonMessageBean)

        fun onApiException(error: Int)

        fun onNewMilestoneStatusSuccessFully(loginParams: CommonMessageBean)

        fun onNewMilestoneStatusFailed(loginParams: CommonMessageBean)


    }

    interface Presenter : BaseContract.Presenter<View> {


        fun getNewMilestoneDetail(milestoneRequestId: String)

        fun milestoneStatusUpdate(params: NewMilestoneStatusUpdateParams)
    }

}