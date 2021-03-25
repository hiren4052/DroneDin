package com.grewon.dronedin.dagger.component


import com.grewon.dronedin.addprofile.AddMoreProfileActivity
import com.grewon.dronedin.addprofile.AddProfileActivity
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.BaseFragment
import com.grewon.dronedin.changepassword.ChangePasswordActivity
import com.grewon.dronedin.clientjobs.ClientJobsFragment
import com.grewon.dronedin.clientjobs.clientoffers.ClientOffersActivity
import com.grewon.dronedin.clientjobs.clientoffers.ClientOffersDetailsActivity
import com.grewon.dronedin.clientjobs.posted.ClientProposalDetailsActivity
import com.grewon.dronedin.clientjobs.posted.ClientProposalsActivity
import com.grewon.dronedin.clientjobs.posted.PostedJobDetailsActivity
import com.grewon.dronedin.dagger.module.*
import com.grewon.dronedin.filter.FilterActivity
import com.grewon.dronedin.filter.FilterResultActivity
import com.grewon.dronedin.forgotpassword.ForgotPasswordActivity
import com.grewon.dronedin.invitepilot.InvitePilotActivity
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.mapscreen.JobsMapScreenActivity
import com.grewon.dronedin.milestone.SubmitMilestoneActivity
import com.grewon.dronedin.notifications.NotificationsFragment
import com.grewon.dronedin.offers.CrateOffersActivity
import com.grewon.dronedin.offers.OffersDetailActivity
import com.grewon.dronedin.pilotactivejobs.PilotActiveJobsActivity
import com.grewon.dronedin.pilotactivejobs.PilotActiveJobsDetailActivity
import com.grewon.dronedin.pilotfindjobs.FindJobsDetailsActivity
import com.grewon.dronedin.pilotfindjobs.PilotFindJobsFragment
import com.grewon.dronedin.pilotmyjobs.PilotMyJobsFragment
import com.grewon.dronedin.postjob.AddJobDetailsFragment
import com.grewon.dronedin.postjob.SelectFragmentFragment
import com.grewon.dronedin.profile.ProfileFragment
import com.grewon.dronedin.proposals.ProposalsDetailActivity
import com.grewon.dronedin.signin.SignInActivity
import com.grewon.dronedin.signup.SignUpActivity
import com.grewon.dronedin.splash.SplashActivity
import com.grewon.dronedin.submitproposal.SubmitProposalActivity
import com.grewon.dronedin.verification.VerificationActivity

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, NetworkModule::class, SignInModule::class, ProfileModule::class, CommonDataModule::class,
        InviteModule::class, ClientJobsModule::class, FilterModule::class, PilotJobsModule::class]
)
interface AppComponent {

    fun inject(activity: BaseActivity)
    fun inject(activity: SignInActivity)
    fun inject(activity: SplashActivity)
    fun inject(activity: ForgotPasswordActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: SignUpActivity)
    fun inject(activity: ChangePasswordActivity)
    fun inject(activity: AddProfileActivity)
    fun inject(activity: AddMoreProfileActivity)
    fun inject(activity: FilterActivity)
    fun inject(activity: InvitePilotActivity)
    fun inject(activity: VerificationActivity)
    fun inject(activity: PostedJobDetailsActivity)
    fun inject(activity: FilterResultActivity)
    fun inject(activity: JobsMapScreenActivity)
    fun inject(activity: FindJobsDetailsActivity)
    fun inject(activity: SubmitProposalActivity)
    fun inject(activity: ProposalsDetailActivity)
    fun inject(activity: ClientProposalsActivity)
    fun inject(activity: ClientProposalDetailsActivity)
    fun inject(activity: CrateOffersActivity)
    fun inject(activity: ClientOffersActivity)
    fun inject(activity: ClientOffersDetailsActivity)
    fun inject(activity: OffersDetailActivity)
    fun inject(activity: PilotActiveJobsActivity)
    fun inject(activity: PilotActiveJobsDetailActivity)
    fun inject(activity: SubmitMilestoneActivity)


    //Fragments

    fun inject(fragment: BaseFragment)
    fun inject(fragment: NotificationsFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: SelectFragmentFragment)
    fun inject(fragment: AddJobDetailsFragment)
    fun inject(fragment: ClientJobsFragment)
    fun inject(fragment: PilotFindJobsFragment)
    fun inject(fragment: PilotMyJobsFragment)


}