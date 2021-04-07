package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.addprofile.contract.AddBioContract
import com.grewon.dronedin.addprofile.contract.AddProfileContract
import com.grewon.dronedin.addprofile.presenter.AddBioPresenter
import com.grewon.dronedin.addprofile.presenter.AddProfilePresenter
import com.grewon.dronedin.clientjobs.contract.*
import com.grewon.dronedin.clientjobs.presenter.*
import com.grewon.dronedin.invitepilot.contract.PilotInviteContract
import com.grewon.dronedin.invitepilot.presenter.PilotInvitePresenter
import com.grewon.dronedin.offers.contract.CreateOffersContract
import com.grewon.dronedin.offers.presenter.CreateOffersPresenter
import com.grewon.dronedin.postjob.contract.JobPostContract
import com.grewon.dronedin.postjob.contract.SkillsEquipmentsContract
import com.grewon.dronedin.postjob.presenter.JobPostPresenter
import com.grewon.dronedin.postjob.presenter.SkillsEquipmentsPresenter

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClientJobsModule {

    @Singleton
    @Provides
    fun provideClientJobsPresenter(): ClientJobsContract.Presenter {
        return ClientJobsPresenter()
    }

    @Singleton
    @Provides
    fun provideClientJobsDetailPresenter(): ClientJobsDetailContract.Presenter {
        return ClientJobDetailsPresenter()
    }

    @Singleton
    @Provides
    fun provideClientProposalPresenter(): ClientProposalContract.Presenter {
        return ClientProposalPresenter()
    }

    @Singleton
    @Provides
    fun provideCreateOfferPresenter(): CreateOffersContract.Presenter {
        return CreateOffersPresenter()
    }


    @Singleton
    @Provides
    fun provideClientOfferPresenter(): ClientOffersContract.Presenter {
        return ClientOffersPresenter()
    }

    @Singleton
    @Provides
    fun provideClientOfferDetailsPresenter(): ClientOffersDetailContract.Presenter {
        return ClientOffersDetailsPresenter()
    }
}