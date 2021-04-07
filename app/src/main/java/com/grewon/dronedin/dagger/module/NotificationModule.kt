package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.addprofile.contract.AddBioContract
import com.grewon.dronedin.addprofile.contract.AddProfileContract
import com.grewon.dronedin.addprofile.presenter.AddBioPresenter
import com.grewon.dronedin.addprofile.presenter.AddProfilePresenter
import com.grewon.dronedin.clientjobs.contract.ClientJobsContract
import com.grewon.dronedin.clientjobs.contract.ClientJobsDetailContract
import com.grewon.dronedin.clientjobs.presenter.ClientJobDetailsPresenter
import com.grewon.dronedin.clientjobs.presenter.ClientJobsPresenter
import com.grewon.dronedin.filter.contract.FilterContract
import com.grewon.dronedin.filter.presenter.FilterPresenter
import com.grewon.dronedin.invitepilot.contract.PilotInviteContract
import com.grewon.dronedin.invitepilot.presenter.PilotInvitePresenter
import com.grewon.dronedin.notifications.contract.NotificationContract
import com.grewon.dronedin.notifications.presenter.NotificationPresenter
import com.grewon.dronedin.postjob.contract.JobPostContract
import com.grewon.dronedin.postjob.contract.SkillsEquipmentsContract
import com.grewon.dronedin.postjob.presenter.JobPostPresenter
import com.grewon.dronedin.postjob.presenter.SkillsEquipmentsPresenter

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotificationModule {

    @Singleton
    @Provides
    fun provideNotificationPresenter(): NotificationContract.Presenter {
        return NotificationPresenter()
    }



}