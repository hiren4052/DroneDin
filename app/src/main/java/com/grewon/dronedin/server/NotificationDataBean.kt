package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize



@SuppressLint("ParcelCreator")
@Parcelize
data class NotificationDataBean(
    @SerializedName("data")
    val `data`: ArrayList<Data>? = null,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("dispute_id")
        val disputeId: String? = "",
        @SerializedName("job_id")
        val jobId: String? = "",
        @SerializedName("milestone_id")
        val milestoneId: String? = "",
        @SerializedName("milestone_request_id")
        val milestoneRequestId: String? = "",
        @SerializedName("notification_datecreated")
        val notificationDatecreated: String? = "",
        @SerializedName("notification_dateupdated")
        val notificationDateupdated: String? = "",
        @SerializedName("notification_id")
        val notificationId: String? = "",
        @SerializedName("notification_message")
        val notificationMessage: String? = "",
        @SerializedName("notification_title")
        val notificationTitle: String? = "",
        @SerializedName("offer_id")
        val offerId: String? = "",
        @SerializedName("proposal_id")
        val proposalId: String? = "",
        @SerializedName("user_name")
        val userName: String? = ""
    ) : Parcelable
}