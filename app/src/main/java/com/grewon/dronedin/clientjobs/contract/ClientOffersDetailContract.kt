package com.grewon.dronedin.clientjobs.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface ClientOffersDetailContract {

    interface View : BaseContract.View {

        fun onOffersDetailSuccessfully(response: OffersDetailBean)

        fun onOffersDetailFailed(loginParams: CommonMessageBean)

        fun onWithdrawOffersSuccessfully(response: CommonMessageBean)

        fun onWithdrawOffersFailed(loginParams: CommonMessageBean)

        fun showOnScreenProgress()

        fun hideOnScreenProgress()

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getOffersDetails(offersId: String, jobType: String)

        fun withDrawOffers(offersId: String)

    }

}