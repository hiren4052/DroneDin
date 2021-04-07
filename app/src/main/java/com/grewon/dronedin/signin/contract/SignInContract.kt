package com.grewon.dronedin.signin.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.LoginParams
import com.grewon.dronedin.server.params.SocialLoginParams


interface SignInContract {

    interface View : BaseContract.View {

        fun onUserLoggedInSuccessful(response: UserData)

        fun onUserLoggedInFailed(loginParams: LoginParams)

        fun onUserSocialLoggedInFailed(loginParams: SocialLoginParams)

        fun onApiException(error: Int)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun userSocialLogin(params: SocialLoginParams)

        fun userLogin(params: LoginParams)


    }

}