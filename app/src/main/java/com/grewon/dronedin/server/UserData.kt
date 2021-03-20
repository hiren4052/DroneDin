package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class UserData(
    @SerializedName("data")
    val `data`: Data? = Data(),
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("bank_verified")
        val bankVerified: String? = "",
        @SerializedName("profile_image")
        val profileImage: String? = "",
        @SerializedName("profile_update")
        val profileUpdate: String? = "",
        @SerializedName("user_api_token")
        val userApiToken: String? = "",
        @SerializedName("user_email")
        val userEmail: String? = "",
        @SerializedName("user_id")
        val userId: String? = "",
        @SerializedName("user_name")
        val userName: String? = "",
        @SerializedName("user_phone_number")
        val userPhoneNumber: String? = "",
        @SerializedName("user_type")
        val userType: String? = "",
        @SerializedName("user_verified")
        val userVerified: String? = "",
        var isStepComplete: Boolean = false
    ) : Parcelable
}