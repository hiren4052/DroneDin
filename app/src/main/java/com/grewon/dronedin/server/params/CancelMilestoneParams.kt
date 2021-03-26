package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CancelMilestoneParams(
    @SerializedName("job_id")
    val jobId: Int? = 0,
    @SerializedName("milestone_cancel_desc")
    val milestoneCancelDesc: String? = "",
    @SerializedName("milestone_id")
    val milestoneId: String? = "",
    @SerializedName("milestone_status")
    val milestoneStatus: String? = ""
) : Parcelable