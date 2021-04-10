package com.grewon.dronedin.membership.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.MemberShipBean
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.*


interface MembershipListContract {

    interface View : BaseContract.View {

        fun onMembershipListSuccessful(response: MemberShipBean)

        fun onMembershipListFailed(loginParams: CommonMessageBean)

        fun onApiException(error: Int)

        fun showOnScreenProgress()

        fun hideOnScreenProgress()

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getMembershipList()


    }

}