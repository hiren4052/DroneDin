package com.grewon.dronedin.project.endproject.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface EndProjectContract {

    interface View : BaseContract.View {


        fun onEndSuccessFully(loginParams: CommonMessageBean)

        fun onEndFailed(loginParams: CancelEndProjectParams)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {


        fun endProject(params:CancelEndProjectParams)


    }

}