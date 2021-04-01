package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ClientProfileBean(
    @SerializedName("profile_image")
    val profileImage: String? = "",
    @SerializedName("profile_update")
    val profileUpdate: String? = "",
    @SerializedName("rate")
    val rate: String? = "",
    @SerializedName("review")
    val review: List<String?>? = null,
    @SerializedName("total_active_job")
    val totalActiveJob: String? = "",
    @SerializedName("total_job")
    val totalJob: String? = "",
    @SerializedName("total_spending")
    val totalSpending: String? = "",
    @SerializedName("user_address")
    val userAddress: String? = "",
    @SerializedName("user_bio")
    val userBio: String? = "",
    @SerializedName("user_latitude")
    val userLatitude: String? = "",
    @SerializedName("user_longitude")
    val userLongitude: String? = "",
    @SerializedName("user_name")
    val userName: String? = "",
    @SerializedName("user_type")
    val userType: String? = ""
) : Parcelable