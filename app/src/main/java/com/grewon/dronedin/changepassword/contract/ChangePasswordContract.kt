package com.grewon.dronedin.changepassword.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.*


interface ChangePasswordContract {

    interface View : BaseContract.View {

        fun onChangePasswordSuccessful(response: CommonMessageBean)

        fun onChangePasswordFailed(loginParams: ChangePasswordParams)

        fun onApiException(error: Int)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun changePassword(params: ChangePasswordParams)

    }

}