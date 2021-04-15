package com.grewon.dronedin.wallet.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface WalletContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onWalletDataGetSuccessful(response: EarningsDataBean)

        fun onWalletDataGetFailed(loginParams: CommonMessageBean)


        fun onWithdrawDataSuccessful(response: CommonMessageBean)

        fun onWithdrawDataFailed(loginParams: WithdrawParams)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getEarningsData(offsetCount:Int)

        fun withdrawAmount(withdrawParams: WithdrawParams)

    }

}