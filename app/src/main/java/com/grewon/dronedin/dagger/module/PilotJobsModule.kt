package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.offers.contract.OffersDetailsContract
import com.grewon.dronedin.offers.presenter.OffersDetailsPresenter
import com.grewon.dronedin.pilotactivejobs.contract.PilotActiveJobsContract
import com.grewon.dronedin.pilotactivejobs.contract.PilotActiveJobsDetailsContract
import com.grewon.dronedin.pilotactivejobs.presenter.PilotActiveJobsDetailsPresenter
import com.grewon.dronedin.pilotactivejobs.presenter.PilotActiveJobsPresenter
import com.grewon.dronedin.pilotfindjobs.contract.PilotJobsContract
import com.grewon.dronedin.pilotfindjobs.presenter.PilotJobsPresenter
import com.grewon.dronedin.pilotjobhistory.contract.PilotHistoryJobsContract
import com.grewon.dronedin.pilotjobhistory.presenter.PilotHistoryJobsPresenter
import com.grewon.dronedin.pilotmyjobs.contract.PilotFindJobsDetailContract
import com.grewon.dronedin.pilotmyjobs.contract.PilotMyJobsContract
import com.grewon.dronedin.pilotmyjobs.presenter.PilotFindJobDetailsPresenter
import com.grewon.dronedin.pilotmyjobs.presenter.PilotMyJobsPresenter
import com.grewon.dronedin.proposals.contract.ProposalsDetailContract
import com.grewon.dronedin.proposals.presenter.ProposalsDetailsPresenter
import com.grewon.dronedin.review.contract.SubmitReviewContract
import com.grewon.dronedin.review.presenter.SubmitReviewPresenter
import com.grewon.dronedin.submitproposal.contract.SubmitProposalContract
import com.grewon.dronedin.submitproposal.presenter.SubmitProposalPresenter

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


    @Singleton
    @Provides
    fun provideSubmitProposalPresenter(): SubmitProposalContract.Presenter {
        return SubmitProposalPresenter()
    }

    @Singleton
    @Provides
    fun provideProposalsDetailsPresenter(): ProposalsDetailContract.Presenter {
        return ProposalsDetailsPresenter()
    }

    @Singleton
    @Provides
    fun provideOffersDetailsPresenter(): OffersDetailsContract.Presenter {
        return OffersDetailsPresenter()
    }

    @Singleton
    @Provides
    fun providePilotActiveJobsPresenter(): PilotActiveJobsContract.Presenter {
        return PilotActiveJobsPresenter()
    }


    @Singleton
    @Provides
    fun providePilotActiveJobsDetailsPresenter(): PilotActiveJobsDetailsContract.Presenter {
        return PilotActiveJobsDetailsPresenter()
    }

    @Singleton
    @Provides
    fun provideSubmitReviewPresenter(): SubmitReviewContract.Presenter {
        return SubmitReviewPresenter()
    }

    @Singleton
    @Provides
    fun providePilotHistoryJobsPresenter(): PilotHistoryJobsContract.Presenter {
        return PilotHistoryJobsPresenter()
    }
}