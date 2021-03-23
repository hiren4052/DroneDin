package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.addprofile.contract.AddBioContract
import com.grewon.dronedin.addprofile.contract.AddProfileContract
import com.grewon.dronedin.addprofile.presenter.AddBioPresenter
import com.grewon.dronedin.addprofile.presenter.AddProfilePresenter
import com.grewon.dronedin.clientjobs.contract.ClientJobsContract
import com.grewon.dronedin.clientjobs.contract.ClientJobsDetailContract
import com.grewon.dronedin.clientjobs.presenter.ClientJobDetailsPresenter
import com.grewon.dronedin.clientjobs.presenter.ClientJobsPresenter
import com.grewon.dronedin.invitepilot.contract.PilotInviteContract
import com.grewon.dronedin.invitepilot.presenter.PilotInvitePresenter
import com.grewon.dronedin.pilotfindjobs.contract.PilotJobsContract
import com.grewon.dronedin.pilotfindjobs.presenter.PilotJobsPresenter
import com.grewon.dronedin.pilotmyjobs.contract.PilotFindJobsDetailContract
import com.grewon.dronedin.pilotmyjobs.contract.PilotMyJobsContract
import com.grewon.dronedin.pilotmyjobs.presenter.PilotFindJobDetailsPresenter
import com.grewon.dronedin.pilotmyjobs.presenter.PilotMyJobsPresenter
import com.grewon.dronedin.postjob.contract.JobPostContract
import com.grewon.dronedin.postjob.contract.SkillsEquipmentsContract
import com.grewon.dronedin.postjob.presenter.JobPostPresenter
import com.grewon.dronedin.postjob.presenter.SkillsEquipmentsPresenter

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PilotJobsModule {

    @Singleton
    @Provides
    fun providePilotJobsPresenter(): PilotJobsContract.Presenter {
        return PilotJobsPresenter()
    }

    @Singleton
    @Provides
    fun providePilotMyJobsPresenter(): PilotMyJobsContract.Presenter {
        return PilotMyJobsPresenter()
    }

    @Singleton
    @Provides
    fun providePilotFindJobDetailsPresenter(): PilotFindJobsDetailContract.Presenter {
        return PilotFindJobDetailsPresenter()
    }
}