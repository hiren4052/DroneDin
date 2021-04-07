package com.grewon.dronedin.offers.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface CreateOffersContract {

    interface View : BaseContract.View {


        fun onSubmitOffersSuccessFully(loginParams: CommonMessageBean)

        fun onSubmitOffersFailed(loginParams: SubmitOfferParams)

        fun onCardDataGetSuccessful(response: CardDataBean)

        fun onCardDataGetFailed(loginParams: CommonMessageBean)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {


        fun submitOffer(params: SubmitOfferParams)

        fun getCardData()

    }

}