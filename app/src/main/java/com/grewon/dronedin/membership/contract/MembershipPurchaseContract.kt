package com.grewon.dronedin.membership.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.MemberShipBean
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.*


interface MembershipPurchaseContract {

    interface View : BaseContract.View {

        fun onMembershipPurchaseSuccessful(response: CommonMessageBean)

        fun onMembershipPurchaseFailed(loginParams: PurchasePackageParams)

        fun onApiException(error: Int)



    }

    interface Presenter : BaseContract.Presenter<View> {

        fun purchaseMemberShip(purchasePackageParams: PurchasePackageParams)


    }

}