package com.grewon.dronedin.forgotpassword.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.*


interface ForgotPasswordContract {

    interface View : BaseContract.View {

        fun onEmailSentSuccessful(response: CommonMessageBean)

        fun onEmailSentFailed(loginParams: ForgotPasswordParams)

        fun onApiException(error: Int)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun forgotPassword(params: ForgotPasswordParams)




    }

}