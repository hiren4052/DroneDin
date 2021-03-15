package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class RegisterParams(
    @SerializedName("user_agree")
    val userAgree: String? = "",
    @SerializedName("user_device")
    val userDevice: String? = "android",
    @SerializedName("user_device_info")
    val userDeviceInfo: String? = "",
    @SerializedName("user_email")
    val userEmail: String? = "",
    @SerializedName("user_fcm_token")
    val userFcmToken: String? = "",
    @SerializedName("user_name")
    val userName: String? = "",
    @SerializedName("user_password")
    val userPassword: String? = "",
    @SerializedName("user_phone_number")
    val userPhoneNumber: String? = "",
    @SerializedName("user_type")
    val userType: String? = ""
) : Parcelable