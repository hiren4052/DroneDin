package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class SocialLoginParams(
    @SerializedName("user_device")
    val userDevice: String?,
    @SerializedName("user_fcm_token")
    val userFcmToken: String?,
    @SerializedName("user_login_type")
    val userLoginType: String? ,
    @SerializedName("user_social_id")
    val userSocialId: String?,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable