package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class ProfileUpdateParams(
    @SerializedName("user_name")
    val userName: String?,
    @SerializedName("user_phone_number")
    val userPhoneNumber: String?,
    @SerializedName("user_address")
    val userAddress: String?,
    @SerializedName("user_latitude")
    val userLatitude: Double?,
    @SerializedName("user_longitude")
    val userLongitude: Double?,
    @SerializedName("proof_id")
    val proofId: String?,
    @SerializedName("proof_front_side")
    val proofFrontSide: String?,
    @SerializedName("proof_back_side")
    val proofBackSide: String?,
    @SerializedName("profile_image")
    val profileImage: String?
) : Parcelable