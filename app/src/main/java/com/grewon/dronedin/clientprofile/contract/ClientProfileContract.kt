package com.grewon.dronedin.clientprofile.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface ClientProfileContract {

    interface View : BaseContract.View {

        fun onClientProfileDetailSuccessfully(response: ClientProfileBean)

        fun onClientProfileDetailFailed(loginParams: CommonMessageBean)

        fun showOnScreenProgress()

        fun hideOnScreenProgress()

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getClientProfile(profileId: String)


    }

}