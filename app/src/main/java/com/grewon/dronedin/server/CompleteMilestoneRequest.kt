package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CompleteMilestoneRequest(
    @SerializedName("attachment")
    val attachment: ArrayList<Attachment>? = null,
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
    data class Attachment(
        @SerializedName("attachment")
        val attachment: String? = "",
        @SerializedName("attachment_id")
        val attachmentId: String? = ""
    ) : Parcelable
}