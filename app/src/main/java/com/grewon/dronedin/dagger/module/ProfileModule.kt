package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.addprofile.contract.AddBioContract
import com.grewon.dronedin.addprofile.contract.AddProfileContract
import com.grewon.dronedin.addprofile.presenter.AddBioPresenter
import com.grewon.dronedin.addprofile.presenter.AddProfilePresenter

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ProfileModule {

    @Singleton
    @Provides
    fun provideAddProfilePresenter(): AddProfileContract.Presenter {
        return AddProfilePresenter()
    }

    @Singleton
    @Provides
    fun provideAddBioPresenter(): AddBioContract.Presenter {
        return AddBioPresenter()
    }

}