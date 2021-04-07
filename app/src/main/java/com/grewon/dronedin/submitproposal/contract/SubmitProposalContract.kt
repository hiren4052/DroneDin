package com.grewon.dronedin.submitproposal.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface SubmitProposalContract {

    interface View : BaseContract.View {


        fun onSubmitProposalSuccessFully(loginParams: CommonMessageBean)

        fun onSubmitProposalFailed(loginParams: SubmitProposalParams)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {


        fun submitProposal(params: SubmitProposalParams)


    }

}