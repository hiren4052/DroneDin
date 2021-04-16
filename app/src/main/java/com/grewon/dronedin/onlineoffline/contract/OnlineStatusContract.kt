package com.grewon.dronedin.onlineoffline.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface OnlineStatusContract {

    interface View : BaseContract {


        fun onApiException(error: Int)

        fun onUserOnlineSuccessful(response: CommonMessageBean)

        fun onUserOnlineFailed(loginParams: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun changeOnlineStatus(onlineStatus: String)

    }

}