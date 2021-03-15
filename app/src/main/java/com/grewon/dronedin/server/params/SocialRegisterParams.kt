package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class SocialRegisterParams(
    @SerializedName("user_device")
    val userDevice: String? = "android",
    @SerializedName("user_device_info")
    val userDeviceInfo: String? = "",
    @SerializedName("user_email")
    val userEmail: String? = "",
    @SerializedName("user_fcm_token")
    val userFcmToken: String? = "",
    @SerializedName("user_login_type")
    val userLoginType: String? = "",
    @SerializedName("user_name")
    val userName: String? = "",
    @SerializedName("user_social_id")
    val userSocialId: String? = "",
    @SerializedName("user_type")
    val userType: String? = ""
) : Parcelable