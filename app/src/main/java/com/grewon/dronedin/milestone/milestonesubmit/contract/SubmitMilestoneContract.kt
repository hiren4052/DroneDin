package com.grewon.dronedin.milestone.milestonesubmit.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface SubmitMilestoneContract {

    interface View : BaseContract.View {


        fun onSubmitSuccessFully(loginParams: CommonMessageBean)

        fun onSubmitFailed(loginParams: SubmitMilestoneParams)



        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {


        fun submitMilestone(params: SubmitMilestoneParams)



    }

}