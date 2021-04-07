package com.grewon.dronedin.paymentsummary.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface ActiveMileStoneContract {

    interface View : BaseContract.View {

        fun onActiveMilestoneSuccessfully(response: CommonMessageBean)

        fun onActiveMilestoneFailed(loginParams: CommonMessageBean)

        fun onCardDataGetSuccessful(response: CardDataBean)

        fun onCardDataGetFailed(loginParams: CommonMessageBean)


        fun onApiException(error: Int)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun activeMileStone(activeMilestoneParams: ActiveMilestoneParams)

        fun getCardData()

    }

}