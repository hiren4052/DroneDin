package com.grewon.dronedin.invitepilot.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface PilotInviteContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onPilotDataGetSuccessful(response: PilotDataBean)

        fun onPilotDataGetFailed(loginParams: CommonMessageBean)

        fun onPilotInviteSuccessful(response: CommonMessageBean)

        fun onPilotInviteFailed(loginParams: PilotInviteParams)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getPilotData(filterParams: FilterParams)

        fun invitePilots(invitePilots: PilotInviteParams)


    }

}