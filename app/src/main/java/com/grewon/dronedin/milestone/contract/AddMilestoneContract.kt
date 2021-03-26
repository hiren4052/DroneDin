package com.grewon.dronedin.milestone.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface AddMilestoneContract {

    interface View : BaseContract.View {


        fun onSubmitSuccessFully(loginParams: CommonMessageBean)

        fun onSubmitFailed(loginParams: AddMileStoneParams)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {


        fun addMilestone(params: AddMileStoneParams)


    }

}