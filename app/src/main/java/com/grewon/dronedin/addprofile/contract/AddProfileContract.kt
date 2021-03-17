package com.grewon.dronedin.addprofile.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.IdentificationBean
import com.grewon.dronedin.server.ProfileBean
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.*


interface AddProfileContract {

    interface View : BaseContract.View {

        fun onProfileGetSuccessful(response: ProfileBean)

        fun onProfileGetFailed(loginParams: ProfileUpdateParams)

        fun onProfileUpdateSuccessFully(loginParams: ProfileBean)

        fun onProfileUpdateFailed(loginParams: ProfileUpdateParams)

        fun onApiException(error: Int)

        fun onIdentificationsDataGetSuccessful(response: IdentificationBean)

        fun onIdentificationsDataFailed(loginParams: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getProfile()

        fun updateProfile(params: ProfileUpdateParams)

        fun getIdentificationsData()


    }

}