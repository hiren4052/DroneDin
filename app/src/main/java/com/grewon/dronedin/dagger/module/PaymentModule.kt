package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.addbank.contract.AddBankContract
import com.grewon.dronedin.addbank.presenter.AddBankPresenter
import com.grewon.dronedin.addcard.contract.AddCardContract
import com.grewon.dronedin.paymentmethod.contract.PaymentMethodContract
import com.grewon.dronedin.addcard.presenter.AddCardPresenter
import com.grewon.dronedin.paymentmethod.presenter.PaymentMethodPresenter

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PaymentModule {

    @Singleton
    @Provides
    fun providePaymentMethodPresenter(): PaymentMethodContract.Presenter {
        return PaymentMethodPresenter()
    }


    @Singleton
    @Provides
    fun provideAddCardPresenter(): AddCardContract.Presenter {
        return AddCardPresenter()
    }

    @Singleton
    @Provides
    fun provideAddBankPresenter(): AddBankContract.Presenter {
        return AddBankPresenter()
    }

}