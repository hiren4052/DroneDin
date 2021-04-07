package com.grewon.dronedin.pilotprofile.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface PilotProfileContract {

    interface View : BaseContract.View {

        fun onProfileDetailSuccessfully(response: PilotProfileBean)

        fun onProfileDetailFailed(loginParams: CommonMessageBean)

        fun showOnScreenProgress()

        fun hideOnScreenProgress()

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getPilotProfile(profileId: String)


    }

}