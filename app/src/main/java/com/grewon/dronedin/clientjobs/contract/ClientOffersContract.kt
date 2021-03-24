package com.grewon.dronedin.clientjobs.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface ClientOffersContract {

    interface View : BaseContract.View {


        fun onOffersGetSuccessful(response: OffersDataBean)

        fun onOffersGetFailed(loginParams: CommonMessageBean)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getClientOffers(jobId: String, page: Int)


    }

}