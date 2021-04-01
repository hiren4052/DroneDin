package com.grewon.dronedin.addcard.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface AddCardContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onCreateCardSuccessful(response: CardDataBean)

        fun onCreateCardFailed(loginParams: AddCardParams)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun createCard(addCardParams: AddCardParams)


    }

}