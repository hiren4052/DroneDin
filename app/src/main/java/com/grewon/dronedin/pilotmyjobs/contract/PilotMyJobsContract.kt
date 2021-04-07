package com.grewon.dronedin.pilotmyjobs.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface PilotMyJobsContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onProposalDataGetSuccessful(response: ProposalsDataBean)

        fun onInvitationsDataGetSuccessful(response: InvitationsDataBean)

        fun onOffersDataGetSuccessful(response: OffersDataBean)

        fun onDataGetFailed(loginParams: CommonMessageBean)
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getPilotOffersMyJobs(filterParams: GetJobsParams)

        fun getPilotInvitationsMyJobs(filterParams: GetJobsParams)

        fun getPilotProposalsMyJobs(filterParams: GetJobsParams)


    }

}