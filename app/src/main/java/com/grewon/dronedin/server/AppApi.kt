package com.grewon.dronedin.server


import com.evereats.app.server.UserData
import com.grewon.dronedin.server.params.LoginParams
import com.grewon.dronedin.server.params.SocialLoginParams
import io.reactivex.Single
import retrofit2.http.*


interface AppApi {

    @POST("user_c/social_login")
    fun socialLogin(@Body map: SocialLoginParams): Single<UserData>

    @POST("user_c/login")
    fun simpleLogin(@Body map: LoginParams): Single<UserData>

}