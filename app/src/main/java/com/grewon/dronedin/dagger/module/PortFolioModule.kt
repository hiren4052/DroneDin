package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.addprofile.contract.AddBioContract
import com.grewon.dronedin.addprofile.contract.AddProfileContract
import com.grewon.dronedin.addprofile.presenter.AddBioPresenter
import com.grewon.dronedin.addprofile.presenter.AddProfilePresenter
import com.grewon.dronedin.portfolio.contract.PortFolioContract
import com.grewon.dronedin.portfolio.presenter.PortFolioPresenter

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PortFolioModule {

    @Singleton
    @Provides
    fun providePortFolioPresenter(): PortFolioContract.Presenter {
        return PortFolioPresenter()
    }



}