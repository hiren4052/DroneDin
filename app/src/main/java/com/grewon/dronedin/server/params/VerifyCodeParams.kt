package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class VerifyCodeParams(
    @SerializedName("user_id")
    val userId: String? = "",
    @SerializedName("user_verification_code")
    val userVerificationCode: String? = "",
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable