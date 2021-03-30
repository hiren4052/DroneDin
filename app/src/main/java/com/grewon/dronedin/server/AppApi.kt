package com.grewon.dronedin.server


import com.grewon.dronedin.server.params.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface AppApi {

    @POST("user_c/social_login")
    fun socialLogin(@Body map: SocialLoginParams): Single<UserData>

    @POST("user_c/login")
    fun simpleLogin(@Body map: LoginParams): Single<UserData>

    @POST("user_c/social_register")
    fun socialRegister(@Body map: SocialRegisterParams): Single<UserData>

    @POST("user_c/register")
    fun simpleRegister(@Body map: RegisterParams): Single<UserData>

    @POST("user_c/forgot_password")
    fun forgotPassword(@Body map: ForgotPasswordParams): Single<CommonMessageBean>

    @POST("user_c/verify_code")
    fun verifyUser(@Body map: VerifyCodeParams): Single<UserData>

    @POST("user_c/resend_verification_code")
    fun resendCode(@Body map: UserIdParams): Single<CommonMessageBean>

    @POST("profile_c/change_password")
    fun changePassword(@Body map: ChangePasswordParams): Single<CommonMessageBean>

    @GET("profile_c/my_profile")
    fun myProfile(): Single<ProfileBean>

    @GET("profile_c/proof")
    fun getIdentificationsData(): Single<IdentificationBean>

    @GET("profile_c/job_init")
    fun getJobCommanData(): Single<JobInitBean>

    @Multipart
    @POST("profile_c/update_profile")
    fun updateProfile(
        @Part profileImage: MultipartBody.Part?,
        @Part frontSide: MultipartBody.Part?,
        @Part backSide: MultipartBody.Part?,
        @PartMap requestPart: HashMap<String, @JvmSuppressWildcards RequestBody?>
    ): Single<ProfileBean>

    @GET("profile_c/bio")
    fun getBio(): Single<ProfileBioDataBean>

    @POST("profile_c/bio_update")
    fun bioUpdate(@Body map: BioUpdateParams): Single<ProfileBioDataBean>


    @POST("job_c/job")
    fun postJob(@Body file: RequestBody): Single<CreateJobsBean>

    @POST("job_c/job_update/{job_id}")
    fun editJob(@Body file: RequestBody, @Path("job_id") jobId: String): Single<CreateJobsBean>

    @GET("pilot_c/pilot_list")
    fun getPilotList(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<PilotDataBean>

    @POST("job_c/send_invitation")
    fun invitePilots(@Body map: PilotInviteParams): Single<CommonMessageBean>


    @GET("job_c/job_list")
    fun getClientJobs(@QueryMap requestPart: HashMap<String, Any>): Single<JobsDataBean>

    @GET("job_c/job")
    fun getPostedJobDetails(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<PostedJobDetailBean>

    @DELETE("job_c/job/{job_id}")
    fun deleteJobs(@Path("job_id") jobId: String): Single<CommonMessageBean>

    @DELETE("job_c/attachment/{attachment_id}")
    fun deleteAttachment(@Path("attachment_id") attachmentId: String): Single<CommonMessageBean>

    @DELETE("job_c/milestone/{milestone_id}")
    fun deleteMilestone(@Path("milestone_id") milestoneId: String): Single<CommonMessageBean>

    @GET("pilot_c/save_pilot/{pilot_id}")
    fun savePilots(@Path("pilot_id") jobId: String): Single<CommonMessageBean>

    @GET("pilot_c/job_list")
    fun getPilotFindJobs(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<PilotJobsDataBean>

    @GET("pilot_c/save_job/{job_id}")
    fun saveJobs(@Path("job_id") jobId: String): Single<CommonMessageBean>

    @GET("pilot_c/my_job")
    fun getPilotOffersJobs(@QueryMap requestPart: HashMap<String, Any>): Single<OffersDataBean>


    @GET("pilot_c/my_job")
    fun getPilotInvitationsJobs(@QueryMap requestPart: HashMap<String, Any>): Single<InvitationsDataBean>

    @GET("pilot_c/my_job")
    fun getPilotProposalsJobs(@QueryMap requestPart: HashMap<String, Any>): Single<ProposalsDataBean>

    @GET("job_c/job")
    fun getPilotFindJobsDetail(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<PilotFindJobsDetailBean>

    @GET("job_c/job")
    fun getProposalDetails(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<ProposalsDetailBean>

    @POST("pilot_c/proposal")
    fun submitProposal(@Body file: RequestBody): Single<CommonMessageBean>

    @GET("pilot_c/withdraw_proposal/{proposal_id}")
    fun withDrawProposals(@Path("proposal_id") proposalId: String): Single<CommonMessageBean>

    @GET("job_c/proposal_list")
    fun getClientProposalList(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<ProposalsDataBean>

    @POST("job_c/offer")
    fun submitOffer(@Body file: RequestBody): Single<CommonMessageBean>

    @GET("job_c/offer_list")
    fun getClientOffers(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<OffersDataBean>


    @GET("job_c/job")
    fun getOffersDetailsBean(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<OffersDetailBean>

    @GET("job_c/withdraw_offer/{offers_id}")
    fun withDrawOffers(@Path("offers_id") offersId: String): Single<CommonMessageBean>

    @GET("pilot_c/offer_acc_dec")
    fun offerAcceptDecline(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<CommonMessageBean>

    @GET("pilot_c/my_job")
    fun getPilotActiveJobs(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<PilotActiveJobsData>

    @GET("job_c/job")
    fun getActiveJobsDetails(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<ActiveJobsDetails>

    @POST("pilot_c/milestone_submit")
    fun submitMilestone(@Body file: RequestBody): Single<CommonMessageBean>

    @GET("job_c/milestone/{milestone_id}")
    fun getMileStoneDetail(@Path("milestone_id") mileStoneId: String): Single<MileStoneDetailsBean>

    @POST("pilot_c/add_milestone")
    fun addMileStone(@FieldMap requestPart: HashMap<String, Any>): Single<CommonMessageBean>

    @POST("job_c/milestone_cancel")
    fun cancelMilestone(@Body file: CancelMilestoneParams): Single<CommonMessageBean>

    @GET("job_c/cancel_project")
    fun cancelProject(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<CommonMessageBean>

    @GET("job_c/end_project")
    fun endProject(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<CommonMessageBean>

    @GET("chat_c/chat_room")
    fun getMessages(): Single<MessagesDataBean>

    @POST("pilot_c/submit_review")
    fun submitReview(@Body params: SubmitReviewParams): Single<CommonMessageBean>

    @POST("profile_c/portfolio")
    fun createPortFolio(@Body file: RequestBody): Single<CommonMessageBean>

    @POST("profile_c/portfolio/{port_folio_id}")
    fun updatePortFolio(
        @Body file: RequestBody,
        @Path("port_folio_id") portFolioId: String
    ): Single<CommonMessageBean>

}