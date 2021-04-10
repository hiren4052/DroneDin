package com.grewon.dronedin.project.endproject.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface EndProjectRequestContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onEndProjectStatusSuccessFully(loginParams: CommonMessageBean)

        fun onEndProjectStatusFailed(loginParams: CommonMessageBean)

        fun onDisputeCreateSuccessFully(loginParams: CommonMessageBean)

        fun onDisputeCreateFailed(loginParams: CreateDisputeParams)
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun projectStatusUpdate(params: CancelEndProjectStatusUpdateParams)

        fun createDispute(params: CreateDisputeParams)

    }

}