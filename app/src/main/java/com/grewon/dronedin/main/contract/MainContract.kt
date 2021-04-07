package com.grewon.dronedin.main.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface MainContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onMainScreenDataGetSuccessful(response: MainScreenData)

        fun onMainScreenDataGetFailed(loginParams: CommonMessageBean)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getMainScreenData(fcmToken:String)

    }

}