package com.grewon.dronedin.settings.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface SettingsContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onLogoutSuccessful(response: CommonMessageBean)

        fun onLogoutFailed(loginParams: CommonMessageBean)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun logoutUser()

    }

}