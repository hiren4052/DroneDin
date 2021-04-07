package com.grewon.dronedin.proposals.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface ProposalsDetailContract {

    interface View : BaseContract.View {

        fun onProposalsDetailSuccessfully(response: ProposalsDetailBean)

        fun onProposalsDetailFailed(loginParams: CommonMessageBean)

        fun onWithdrawProposalsSuccessfully(response: CommonMessageBean)

        fun onWithdrawProposalsFailed(loginParams: CommonMessageBean)

        fun showOnScreenProgress()

        fun hideOnScreenProgress()

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getProposalsDetails(proposalsId: String, jobType: String)

        fun withDrawProposals(proposalsId: String)

    }

}