package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class LoginParams(
    @SerializedName("user_device")
    val userDevice: String?,
    @SerializedName("user_fcm_token")
    val userFcmToken: String?,
    @SerializedName("user_name")
    val userName: String?,
    @SerializedName("user_password")
    val userPassword: String?
) : Parcelable