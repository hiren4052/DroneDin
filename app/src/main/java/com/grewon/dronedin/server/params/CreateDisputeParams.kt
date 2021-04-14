package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CreateDisputeParams(
    @SerializedName("dispute_reason")
    val dispute_reason: String? = "",
    @SerializedName("job_id")
    val job_id: String? = "",
    @SerializedName("job_request_id")
    val job_request_id: String? = "",
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable