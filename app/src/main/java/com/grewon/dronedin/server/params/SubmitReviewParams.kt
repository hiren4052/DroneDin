package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class SubmitReviewParams(
    @SerializedName("job_id")
    val jobId: String? = "",
    @SerializedName("rate")
    val rate: String? = "",
    @SerializedName("review")
    val review: String? = "",
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable