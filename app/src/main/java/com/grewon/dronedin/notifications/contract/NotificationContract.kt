package com.grewon.dronedin.notifications.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface NotificationContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onNotificationGetSuccessful(response: NotificationDataBean)

        fun onNotificationGetFailed(loginParams: CommonMessageBean)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getNotification(offset: String)


    }

}