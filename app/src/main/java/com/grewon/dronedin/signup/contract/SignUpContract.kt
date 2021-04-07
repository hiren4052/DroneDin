package com.grewon.dronedin.signup.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.LoginParams
import com.grewon.dronedin.server.params.RegisterParams
import com.grewon.dronedin.server.params.SocialLoginParams
import com.grewon.dronedin.server.params.SocialRegisterParams


interface SignUpContract {

    interface View : BaseContract.View {

        fun onUserRegisterSuccessful(response: UserData)

        fun onUserRegisterFailed(loginParams: RegisterParams)

        fun onUserSocialRegisterFailed(loginParams: SocialRegisterParams)

        fun onApiException(error: Int)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun userSocialRegister(params: SocialRegisterParams)

        fun userRegister(params: RegisterParams)


    }

}