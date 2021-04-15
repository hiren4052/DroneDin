package com.grewon.dronedin.addprofile.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface AddBioContract {

    interface View : BaseContract.View {

        fun onBioGetSuccessful(response: ProfileBioDataBean)

        fun onBioGetFailed(loginParams: CommonMessageBean)

        fun onBioUpdateSuccessFully(loginParams: ProfileBioDataBean)

        fun onBioUpdateFailed(loginParams: BioUpdateErrorParams)

        fun onApiException(error: Int)

        fun onJobCommonDataGetSuccessful(response: JobInitBean)

        fun onJobCommonDataGetFailed(loginParams: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getJobCommonData()

        fun updateBio(params: BioUpdateParams)

        fun getBio()

    }

}