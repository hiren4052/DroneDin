package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class NewMilestoneRequest(
    @SerializedName("milestone")
    val milestone: ArrayList<Milestone>? = null,
    @SerializedName("milestone_id")
    val milestoneId: String? = "",
    @SerializedName("milestone_request_id")
    val milestoneRequestId: String? = "",
    @SerializedName("milestone_request_note")
    val milestoneRequestNote: String? = "",
    @SerializedName("milestone_request_status")
    val milestoneRequestStatus: String? = "",
    @SerializedName("milestone_request_type")
    val milestoneRequestType: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Milestone(
        @SerializedName("milestone_details")
        val milestoneDetails: String? = "",
        @SerializedName("milestone_id")
        val milestoneId: String? = "",
        @SerializedName("milestone_price")
        val milestonePrice: String? = ""
    ) : Parcelable
}