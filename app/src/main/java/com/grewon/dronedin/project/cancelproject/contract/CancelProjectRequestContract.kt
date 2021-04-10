package com.grewon.dronedin.project.cancelproject.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface CancelProjectRequestContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onCancelProjectStatusSuccessFully(loginParams: CommonMessageBean)

        fun onCancelProjectStatusFailed(loginParams: CommonMessageBean)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun projectStatusUpdate(params: CancelEndProjectStatusUpdateParams)

    }

}