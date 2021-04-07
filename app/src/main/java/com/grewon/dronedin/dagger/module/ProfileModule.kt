package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.addprofile.contract.AddBioContract
import com.grewon.dronedin.addprofile.contract.AddProfileContract
import com.grewon.dronedin.addprofile.presenter.AddBioPresenter
import com.grewon.dronedin.addprofile.presenter.AddProfilePresenter
import com.grewon.dronedin.clientprofile.contract.ClientProfileContract
import com.grewon.dronedin.clientprofile.presenter.ClientProfilePresenter
import com.grewon.dronedin.pilotprofile.contract.PilotProfileContract
import com.grewon.dronedin.pilotprofile.presenter.PilotProfilePresenter

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

    @Singleton
    @Provides
    fun providePilotProfilePresenter(): PilotProfileContract.Presenter {
        return PilotProfilePresenter()
    }

    @Singleton
    @Provides
    fun provideClientProfilePresenter(): ClientProfileContract.Presenter {
        return ClientProfilePresenter()
    }

}