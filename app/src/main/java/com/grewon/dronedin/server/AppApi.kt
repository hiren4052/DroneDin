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
        @Part frontSide: MultipartBody.Part?,
        @Part backSide: MultipartBody.Part?,
        @PartMap requestPart: HashMap<String, @JvmSuppressWildcards RequestBody?>
    ): Single<ProfileBean>

    @GET("profile_c/bio")
    fun getBio(): Single<ProfileBioDataBean>

    @POST("profile_c/bio_update")
    fun bioUpdate(@Body map: BioUpdateParams): Single<ProfileBioDataBean>


    @Multipart
    @POST("job_c/job")
    fun postJob(
        @Part attachmentsList: ArrayList<MultipartBody.Part>?,
        @PartMap requestPart: HashMap<String, @JvmSuppressWildcards RequestBody?>
    ): Single<CreateJobsBean>

    @GET("pilot_c/pilot_list")
    fun getPilotList(
        @QueryMap requestPart: HashMap<String, Any>
    ): Single<PilotDataBean>

    @POST("job_c/send_invitation")
    fun invitePilots(@Body map: PilotInviteParams): Single<CommonMessageBean>

}