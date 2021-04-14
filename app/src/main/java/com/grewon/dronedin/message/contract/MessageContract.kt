package com.grewon.dronedin.message.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface MessageContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onMessageGetSuccessful(response: MessagesDataBean)

        fun onMessageGetFailed(loginParams: CommonMessageBean)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getMessages(offset:Int)




    }

}