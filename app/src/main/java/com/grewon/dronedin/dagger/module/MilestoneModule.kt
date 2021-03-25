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
import com.grewon.dronedin.milestone.contract.MileStoneDetailContract
import com.grewon.dronedin.milestone.contract.SubmitMilestoneContract
import com.grewon.dronedin.milestone.presenter.MileStoneDetailPresenter
import com.grewon.dronedin.milestone.presenter.SubmitMilestonePresenter
import com.grewon.dronedin.offers.contract.OffersDetailsContract
import com.grewon.dronedin.offers.presenter.OffersDetailsPresenter
import com.grewon.dronedin.pilotactivejobs.contract.PilotActiveJobsContract
import com.grewon.dronedin.pilotactivejobs.contract.PilotActiveJobsDetailsContract
import com.grewon.dronedin.pilotactivejobs.presenter.PilotActiveJobsDetailsPresenter
import com.grewon.dronedin.pilotactivejobs.presenter.PilotActiveJobsPresenter
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
import com.grewon.dronedin.proposals.contract.ProposalsDetailContract
import com.grewon.dronedin.proposals.presenter.ProposalsDetailsPresenter
import com.grewon.dronedin.submitproposal.contract.SubmitProposalContract
import com.grewon.dronedin.submitproposal.presenter.SubmitProposalPresenter

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MilestoneModule {

    @Singleton
    @Provides
    fun provideSubmitMilestonePresenter(): SubmitMilestoneContract.Presenter {
        return SubmitMilestonePresenter()
    }


    @Singleton
    @Provides
    fun provideMileStoneDetailPresenter(): MileStoneDetailContract.Presenter {
        return MileStoneDetailPresenter()
    }

}