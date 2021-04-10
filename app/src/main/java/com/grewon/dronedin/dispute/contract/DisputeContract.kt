package com.grewon.dronedin.dispute.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface DisputeContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onDisputeDataGetSuccessful(response: DisputeBean)

        fun onDisputeDataGetFailed(loginParams: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getDispute(offsetCount:Int)

    }

}