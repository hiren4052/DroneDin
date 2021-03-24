package com.grewon.dronedin.clientjobs.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface ClientProposalContract {

    interface View : BaseContract.View {


        fun onProposalGetSuccessful(response: ProposalsDataBean)

        fun onProposalGetFailed(loginParams: CommonMessageBean)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getClientProposals(jobId: String, page: Int)


    }

}