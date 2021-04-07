package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class ReviewsDataBean(
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("profile_image")
    val profileImage: String? = null,
    @SerializedName("rate")
    val rate: String? = null,
    @SerializedName("review")
    val review: String? = null,
    @SerializedName("user_name")
    val userName: String? = null
) : Parcelable
