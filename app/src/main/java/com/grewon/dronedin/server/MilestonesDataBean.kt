package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class MilestonesDataBean(
    @SerializedName("milestone_completed_date")
    var milestoneCompletedDate: String? = "",
    @SerializedName("milestone_details")
    var milestoneDetails: String? = "",
    @SerializedName("milestone_id")
    var milestoneId: String? = "",
    @SerializedName("milestone_price")
    var milestonePrice: String? = "",
    @SerializedName("milestone_request_id")
    var milestoneRequestId: String? = "",
    @SerializedName("milestone_started_date")
    var milestoneStartedDate: String? = "",
    @SerializedName("milestone_status")
    var milestoneStatus: String? = ""
) : Parcelable