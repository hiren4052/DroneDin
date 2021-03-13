package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.signin.contract.SignInContract
import com.grewon.dronedin.signin.presenter.SignInPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SignInModule {

    @Singleton
    @Provides
    fun provideSignInPresenter(): SignInContract.Presenter {
        return SignInPresenter()
    }

}