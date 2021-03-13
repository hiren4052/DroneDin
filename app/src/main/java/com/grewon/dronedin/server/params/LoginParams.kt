package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class LoginParams(
    @SerializedName("user_device")
    val userDevice: String? = "android",
    @SerializedName("user_fcm_token")
    val userFcmToken: String? = "test",
    @SerializedName("user_name")
    val userName: String? = "krishna2@gmail.com",
    @SerializedName("user_password")
    val userPassword: String? = "test123123",
    @SerializedName("user_type")
    val userType: String? = "pilot"
) : Parcelable