package com.grewon.dronedin.dagger.component


import com.grewon.dronedin.addbank.AddBankAccountActivity
import com.grewon.dronedin.addcard.AddCardActivity
import com.grewon.dronedin.addprofile.AddMoreProfileActivity
import com.grewon.dronedin.addprofile.AddProfileActivity
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.BaseFragment
import com.grewon.dronedin.changepassword.ChangePasswordActivity
import com.grewon.dronedin.clientjobs.ClientJobsFragment
import com.grewon.dronedin.clientjobs.active.ClientActiveJobsDetailsActivity
import com.grewon.dronedin.clientjobs.clientoffers.ClientOffersActivity
import com.grewon.dronedin.clientjobs.clientoffers.ClientOffersDetailsActivity
import com.grewon.dronedin.clientjobs.history.ClientJobHistoryDetailsActivity
import com.grewon.dronedin.clientjobs.posted.ClientProposalDetailsActivity
import com.grewon.dronedin.clientjobs.posted.ClientProposalsActivity
import com.grewon.dronedin.clientjobs.posted.PostedJobDetailsActivity
import com.grewon.dronedin.clientprofile.ClientProfileActivity
import com.grewon.dronedin.dagger.module.*
import com.grewon.dronedin.dispute.DisputeActivity
import com.grewon.dronedin.dispute.DisputeChatActivity
import com.grewon.dronedin.filter.FilterActivity
import com.grewon.dronedin.filter.FilterResultActivity
import com.grewon.dronedin.forgotpassword.ForgotPasswordActivity
import com.grewon.dronedin.invitations.InvitationsDetailActivity
import com.grewon.dronedin.invitepilot.InvitePilotActivity
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.mapscreen.JobsMapScreenActivity
import com.grewon.dronedin.membership.MemberShipActivity
import com.grewon.dronedin.membership.MembershipPurchaseActivity
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.message.MessageFragment
import com.grewon.dronedin.milestone.*
import com.grewon.dronedin.milestone.milestoneadd.MilestoneAddActivity
import com.grewon.dronedin.milestone.milestoneaddrequest.MilestoneAddRejectActivity
import com.grewon.dronedin.milestone.milestoneaddrequest.MilestoneAddRequestActivity
import com.grewon.dronedin.milestone.milestonecancel.CancelMilestoneActivity
import com.grewon.dronedin.milestone.milestonecancel.MilestoneCancelRejectActivity
import com.grewon.dronedin.milestone.milestonecancel.MilestoneCancelRequestActivity
import com.grewon.dronedin.milestone.milestonecomplete.MilestoneCompletionRequestActivity
import com.grewon.dronedin.milestone.milestonecomplete.MilestoneRejectActivity
import com.grewon.dronedin.milestone.milestonesubmit.SubmitMilestoneActivity
import com.grewon.dronedin.notifications.NotificationsFragment
import com.grewon.dronedin.offers.CrateOffersActivity
import com.grewon.dronedin.offers.OffersDetailActivity
import com.grewon.dronedin.paymentmethod.PaymentMethodActivity
import com.grewon.dronedin.paymentsummary.MilestoneSummaryActivity
import com.grewon.dronedin.paymentsummary.PaymentSummaryActivity
import com.grewon.dronedin.pilotactivejobs.PilotActiveJobsActivity
import com.grewon.dronedin.pilotactivejobs.PilotActiveJobsDetailActivity
import com.grewon.dronedin.pilotfindjobs.FindJobsDetailsActivity
import com.grewon.dronedin.pilotfindjobs.PilotFindJobsFragment
import com.grewon.dronedin.pilotjobhistory.PilotHistoryDetailsActivity
import com.grewon.dronedin.pilotjobhistory.PilotJobHistoryActivity
import com.grewon.dronedin.pilotmyjobs.PilotMyJobsFragment
import com.grewon.dronedin.pilotprofile.PilotProfileActivity
import com.grewon.dronedin.portfolio.PortFolioActivity
import com.grewon.dronedin.postjob.AddJobDetailsFragment
import com.grewon.dronedin.postjob.SelectFragmentFragment
import com.grewon.dronedin.profile.ProfileFragment
import com.grewon.dronedin.project.cancelproject.CancelProjectActivity
import com.grewon.dronedin.project.cancelproject.ProjectCancelRejectActivity
import com.grewon.dronedin.project.cancelproject.ProjectCancelRequestActivity
import com.grewon.dronedin.project.endproject.EndProjectActivity
import com.grewon.dronedin.project.endproject.ProjectEndRequestActivity
import com.grewon.dronedin.proposals.ProposalsDetailActivity
import com.grewon.dronedin.review.SubmitReviewActivity
import com.grewon.dronedin.savedjobs.SavedJobsActivity
import com.grewon.dronedin.savedpilots.SavedPilotsActivity
import com.grewon.dronedin.settings.SettingsFragment
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
        InviteModule::class, ClientJobsModule::class, FilterModule::class, PilotJobsModule::class, MilestoneModule::class, PaymentModule::class,
        MessageModule::class, PortFolioModule::class, NotificationModule::class, MainModule::class, MembershipModule::class, DisputeModule::class]
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
    fun inject(activity: MilestoneDetailActivity)
    fun inject(activity: ClientActiveJobsDetailsActivity)
    fun inject(activity: MilestoneAddActivity)
    fun inject(activity: CancelMilestoneActivity)
    fun inject(activity: CancelProjectActivity)
    fun inject(activity: EndProjectActivity)
    fun inject(activity: SubmitReviewActivity)
    fun inject(activity: PilotProfileActivity)
    fun inject(activity: PortFolioActivity)
    fun inject(activity: ClientProfileActivity)
    fun inject(activity: ChatActivity)
    fun inject(activity: PaymentMethodActivity)
    fun inject(activity: AddCardActivity)
    fun inject(activity: AddBankAccountActivity)
    fun inject(activity: MilestoneSummaryActivity)
    fun inject(activity: PaymentSummaryActivity)
    fun inject(activity: InvitationsDetailActivity)
    fun inject(activity: SavedJobsActivity)
    fun inject(activity: SavedPilotsActivity)
    fun inject(activity: MilestoneCompletionRequestActivity)
    fun inject(activity: MilestoneRejectActivity)
    fun inject(activity: MilestoneAddRequestActivity)
    fun inject(activity: MilestoneAddRejectActivity)
    fun inject(activity: MilestoneCancelRejectActivity)
    fun inject(activity: MilestoneCancelRequestActivity)
    fun inject(activity: ClientJobHistoryDetailsActivity)
    fun inject(activity: PilotJobHistoryActivity)
    fun inject(activity: PilotHistoryDetailsActivity)
    fun inject(activity: ProjectCancelRejectActivity)
    fun inject(activity: ProjectCancelRequestActivity)
    fun inject(activity: ProjectEndRequestActivity)
    fun inject(activity: MemberShipActivity)
    fun inject(activity: MembershipPurchaseActivity)
    fun inject(activity: DisputeChatActivity)
    fun inject(activity: DisputeActivity)


    //Fragments

    fun inject(fragment: BaseFragment)
    fun inject(fragment: NotificationsFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: SelectFragmentFragment)
    fun inject(fragment: AddJobDetailsFragment)
    fun inject(fragment: ClientJobsFragment)
    fun inject(fragment: PilotFindJobsFragment)
    fun inject(fragment: PilotMyJobsFragment)
    fun inject(fragment: MessageFragment)
    fun inject(fragment: SettingsFragment)

}