package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CancelMilestoneParams(
    @SerializedName("job_id")
    var jobId: String? = "",
    @SerializedName("milestone_cancel_desc")
    var milestoneCancelDesc: String? = "",
    @SerializedName("milestone_id")
    var milestoneId: String? = "",
    @SerializedName("milestone_status")
    var milestoneStatus: String? = "",
    @SerializedName("msg")
    var msg: String? = ""
) : Parcelable