package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.addprofile.contract.AddBioContract
import com.grewon.dronedin.addprofile.contract.AddProfileContract
import com.grewon.dronedin.addprofile.presenter.AddBioPresenter
import com.grewon.dronedin.addprofile.presenter.AddProfilePresenter
import com.grewon.dronedin.postjob.contract.SkillsEquipmentsContract
import com.grewon.dronedin.postjob.presenter.SkillsEquipmentsPresenter

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonDataModule {

    @Singleton
    @Provides
    fun provideSkillsEquipmentsPresenter(): SkillsEquipmentsContract.Presenter {
        return SkillsEquipmentsPresenter()
    }


}