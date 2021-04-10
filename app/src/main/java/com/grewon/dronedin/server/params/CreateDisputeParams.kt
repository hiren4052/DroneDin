package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CreateDisputeParams(
    @SerializedName("dispute_reason")
    val disputeReason: String? = "",
    @SerializedName("job_id")
    val jobId: String? = "",
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable