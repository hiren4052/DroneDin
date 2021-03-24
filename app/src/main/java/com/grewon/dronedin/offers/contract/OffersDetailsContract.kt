package com.grewon.dronedin.offers.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface OffersDetailsContract {

    interface View : BaseContract.View {

        fun onOffersDetailSuccessfully(response: OffersDetailBean)

        fun onOffersDetailFailed(loginParams: CommonMessageBean)

        fun onOffersStatusChangedSuccessfully(response: CommonMessageBean)

        fun onOffersStatusChangedFailed(loginParams: CommonMessageBean)

        fun showOnScreenProgress()

        fun hideOnScreenProgress()

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getOffersDetails(offersId: String, jobType: String)

        fun acceptDeclineOffers(offersId: String, offerStatus: String)

    }

}