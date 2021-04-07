package com.grewon.dronedin.postjob.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface SkillsEquipmentsContract {

    interface View : BaseContract.View {



        fun onApiException(error: Int)

        fun onJobCommonDataGetSuccessful(response: JobInitBean)

        fun onJobCommonDataGetFailed(loginParams: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getJobCommonData()



    }

}