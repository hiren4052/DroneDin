package com.grewon.dronedin.paymentmethod.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface PaymentMethodContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onCardDataGetSuccessful(response: CardDataBean)

        fun onCardDataGetFailed(loginParams: CommonMessageBean)

        fun onCardSelectSuccessfully(loginParams: CommonMessageBean)

        fun onCardSelectFailed(loginParams: CommonMessageBean)

        fun onBankDataGetSuccessful(response: BankDataBean)

        fun onBankDataGetFailed(loginParams: CommonMessageBean)

        fun onBankSelectSuccessfully(loginParams: CommonMessageBean)

        fun onBankSelectFailed(loginParams: CommonMessageBean)

        fun onShowScreenProgress()

        fun onHideScreenProgress()

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getCardData()

        fun setDefaultCardData(defaultCardParams: DefaultCardParams)

        fun getBankData()

        fun setDefaultBankData(defaultCardParams: DefaultCardParams)
    }

}