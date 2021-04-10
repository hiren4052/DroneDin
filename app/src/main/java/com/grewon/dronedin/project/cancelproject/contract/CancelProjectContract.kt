package com.grewon.dronedin.project.cancelproject.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface CancelProjectContract {

    interface View : BaseContract.View {


        fun onCancelSuccessFully(loginParams: CommonMessageBean)

        fun onCancelFailed(loginParams: CancelEndProjectParams)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {


        fun cancelProject(params:CancelEndProjectParams)


    }

}