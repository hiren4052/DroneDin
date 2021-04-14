package com.grewon.dronedin.dispute.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface DisputeDetailsContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onDisputeDetailsGetSuccessful(response: DisputeDetailsBean)

        fun onDisputeDetailsGetFailed(loginParams: CommonMessageBean)
        fun showOnScreenProgress()

        fun hideOnScreenProgress()

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getDisputeDetails(disputeId:String)

    }

}