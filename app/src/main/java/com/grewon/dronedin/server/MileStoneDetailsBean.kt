package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class MileStoneDetailsBean(
    @SerializedName("milestone_cancelled_date")
    val milestoneCancelledDate: String? = "",
    @SerializedName("milestone_completed_date")
    val milestoneCompletedDate: String? = "",
    @SerializedName("milestone_details")
    val milestoneDetails: String? = "",
    @SerializedName("milestone_id")
    val milestoneId: String? = "",
    @SerializedName("milestone_price")
    val milestonePrice: String? = "",
    @SerializedName("milestone_request_id")
    val milestoneRequestId: String? = "",
    @SerializedName("milestone_request_note")
    val milestoneRequestNote: String? = "",
    @SerializedName("milestone_started_date")
    val milestoneStartedDate: String? = "",
    @SerializedName("milestone_status")
    val milestoneStatus: String? = "",
    @SerializedName("attachment")
    val attachment: ArrayList<PilotFindJobsDetailBean.Attachment>? = null,
) : Parcelable{
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Attachment(
        @SerializedName("attachment")
        val attachment: String? = "",
        @SerializedName("attachment_id")
        val attachmentId: String? = ""
    ) : Parcelable
}