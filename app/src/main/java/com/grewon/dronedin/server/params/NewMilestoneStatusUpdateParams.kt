package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class NewMilestoneStatusUpdateParams(
    @SerializedName("milestone_request_id")
    var milestoneRequestId: String? = "",
    @SerializedName("milestone_request_reject_reason")
    var milestoneRequestRejectReason: String? = "",
    @SerializedName("milestone_request_status")
    var milestoneRequestStatus: String? = ""
) : Parcelable