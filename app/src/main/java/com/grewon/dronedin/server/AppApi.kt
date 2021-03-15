package com.grewon.dronedin.server


import com.grewon.dronedin.server.params.*
import io.reactivex.Single
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
}