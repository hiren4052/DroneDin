package com.grewon.dronedin.verification.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface VerificationContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onVerificationSuccessful(response: UserData)

        fun onVerificationFailed(loginParams: VerifyCodeParams)

        fun onResendCodeSuccessful(response: CommonMessageBean)

        fun onResendCodeFailed(loginParams: UserIdParams)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun verifyUser(verifyCodeParams: VerifyCodeParams)

        fun resendCode(userIdParams: UserIdParams)

    }

}