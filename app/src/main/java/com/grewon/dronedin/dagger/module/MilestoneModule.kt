package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.milestone.contract.*
import com.grewon.dronedin.milestone.milestoneadd.contract.AddMilestoneContract
import com.grewon.dronedin.milestone.milestoneadd.presenter.AddMilestonePresenter
import com.grewon.dronedin.milestone.milestoneaddrequest.contract.NewMilestoneRequestContract
import com.grewon.dronedin.milestone.milestoneaddrequest.presenter.NewMilestoneRequestPresenter
import com.grewon.dronedin.milestone.milestonecancel.contract.CancelMilestoneContract
import com.grewon.dronedin.milestone.milestonecancel.contract.CancelMilestoneRequestContract
import com.grewon.dronedin.milestone.milestonecancel.presenter.CancelMilestonePresenter
import com.grewon.dronedin.milestone.milestonecancel.presenter.CancelMilestoneRequestPresenter
import com.grewon.dronedin.milestone.milestonecomplete.contract.CompleteMilestoneContract
import com.grewon.dronedin.milestone.milestonesubmit.contract.SubmitMilestoneContract
import com.grewon.dronedin.milestone.milestonecomplete.presenter.CompleteMilestonePresenter
import com.grewon.dronedin.milestone.milestonesubmit.presenter.SubmitMilestonePresenter
import com.grewon.dronedin.milestone.presenter.*
import com.grewon.dronedin.paymentsummary.contract.ActiveMileStoneContract
import com.grewon.dronedin.paymentsummary.presenter.ActiveMileStonePresenter
import com.grewon.dronedin.project.cancelproject.contract.CancelProjectContract
import com.grewon.dronedin.project.cancelproject.contract.CancelProjectRequestContract
import com.grewon.dronedin.project.cancelproject.presenter.CancelProjectPresenter
import com.grewon.dronedin.project.cancelproject.presenter.CancelProjectRequestPresenter
import com.grewon.dronedin.project.endproject.contract.EndProjectContract
import com.grewon.dronedin.project.endproject.contract.EndProjectRequestContract
import com.grewon.dronedin.project.endproject.presenter.EndProjectPresenter
import com.grewon.dronedin.project.endproject.presenter.EndProjectRequestPresenter

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


    @Singleton
    @Provides
    fun provideAddMilestonePresenter(): AddMilestoneContract.Presenter {
        return AddMilestonePresenter()
    }

    @Singleton
    @Provides
    fun provideCancelMilestonePresenter(): CancelMilestoneContract.Presenter {
        return CancelMilestonePresenter()
    }

    @Singleton
    @Provides
    fun provideCancelProjectPresenter(): CancelProjectContract.Presenter {
        return CancelProjectPresenter()
    }

    @Singleton
    @Provides
    fun provideEndProjectPresenter(): EndProjectContract.Presenter {
        return EndProjectPresenter()
    }

    @Singleton
    @Provides
    fun provideActiveMileStonePresenter(): ActiveMileStoneContract.Presenter {
        return ActiveMileStonePresenter()
    }


    @Singleton
    @Provides
    fun provideCompleteMilestonePresenter(): CompleteMilestoneContract.Presenter {
        return CompleteMilestonePresenter()
    }

    @Singleton
    @Provides
    fun provideNewMilestonePresenter(): NewMilestoneRequestContract.Presenter {
        return NewMilestoneRequestPresenter()
    }


    @Singleton
    @Provides
    fun provideCancelMilestoneRequestPresenter(): CancelMilestoneRequestContract.Presenter {
        return CancelMilestoneRequestPresenter()
    }

    @Singleton
    @Provides
    fun provideCancelProjectRequestPresenter(): CancelProjectRequestContract.Presenter {
        return CancelProjectRequestPresenter()
    }

    @Singleton
    @Provides
    fun provideEndProjectRequestPresenter(): EndProjectRequestContract.Presenter {
        return EndProjectRequestPresenter()
    }

}