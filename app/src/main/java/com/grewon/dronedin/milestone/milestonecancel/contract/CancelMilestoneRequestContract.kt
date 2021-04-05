package com.grewon.dronedin.milestone.milestonecancel.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface CancelMilestoneRequestContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onCancelMilestoneStatusSuccessFully(loginParams: CommonMessageBean)

        fun onCancelMilestoneStatusFailed(loginParams: CommonMessageBean)


    }

    interface Presenter : BaseContract.Presenter<View> {




        fun milestoneStatusUpdate(params: CancelMilestoneStatusUpdateParams)
    }

}