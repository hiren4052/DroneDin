package com.grewon.dronedin.customersupport.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.*


interface CustomerSupportContract {

    interface View : BaseContract.View {

        fun onSendSuccessful(response: CommonMessageBean)

        fun onSendFailed(loginParams: CustomerSupportParams)

        fun onApiException(error: Int)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun sendToSupport(params: CustomerSupportParams)

    }

}