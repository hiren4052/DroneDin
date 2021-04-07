package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.addprofile.contract.AddBioContract
import com.grewon.dronedin.addprofile.contract.AddProfileContract
import com.grewon.dronedin.addprofile.presenter.AddBioPresenter
import com.grewon.dronedin.addprofile.presenter.AddProfilePresenter
import com.grewon.dronedin.invitepilot.contract.PilotInviteContract
import com.grewon.dronedin.invitepilot.presenter.PilotInvitePresenter
import com.grewon.dronedin.postjob.contract.JobPostContract
import com.grewon.dronedin.postjob.contract.SkillsEquipmentsContract
import com.grewon.dronedin.postjob.presenter.JobPostPresenter
import com.grewon.dronedin.postjob.presenter.SkillsEquipmentsPresenter

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InviteModule {

    @Singleton
    @Provides
    fun providePilotInvitePresenter(): PilotInviteContract.Presenter {
        return PilotInvitePresenter()
    }


    @Singleton
    @Provides
    fun provideJobPostPresenter(): JobPostContract.Presenter {
        return JobPostPresenter()
    }

}