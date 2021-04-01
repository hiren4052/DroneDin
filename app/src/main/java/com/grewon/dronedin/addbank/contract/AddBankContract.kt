package com.grewon.dronedin.addbank.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface AddBankContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onCreateBankSuccessful(response: BankDataBean)

        fun onCreateBankFailed(loginParams: AddBankParams)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun createBank(addCardParams: AddBankParams)


    }

}