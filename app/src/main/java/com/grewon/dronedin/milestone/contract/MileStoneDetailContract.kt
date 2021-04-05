package com.grewon.dronedin.milestone.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface MileStoneDetailContract {

    interface View : BaseContract.View {


        fun onDataGetSuccessFully(loginParams: MileStoneDetailsBean)

        fun onDataGetFailed(loginParams: CommonMessageBean)

        fun onCancelSuccessFully(loginParams: CommonMessageBean)

        fun onCancelFailed(loginParams: CancelMilestoneParams)

        fun onApiException(error: Int)

        fun showOnScreenProgress()

        fun hideOnScreenProgress()

    }

    interface Presenter : BaseContract.Presenter<View> {


        fun getMilesStoneDetail(mileStoneId: String)


        fun cancelMilestone(params: CancelMilestoneParams)
    }

}