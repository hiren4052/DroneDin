package com.grewon.dronedin.server

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class DisputeBean(
    @SerializedName("data")
    val `data`: ArrayList<Data>? = null,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("category_name")
        val categoryName: String? = "",
        @SerializedName("client_name")
        val clientName: String? = "",
        @SerializedName("pilot_name")
        val pilotName: String? = "",
        @SerializedName("dispute_id")
        val disputeId: String? = "",
        @SerializedName("dispute_reason")
        val disputeReason: String? = "",
        @SerializedName("dispute_status")
        val disputeStatus: String? = "",
        @SerializedName("job_title")
        val jobTitle: String? = "",
        @SerializedName("milestone_price")
        val milestonePrice: String? = "0",
        @SerializedName("sender_id")
        val senderId: String? = "",
        @SerializedName("sender_type")
        val senderType: String? = "",
        @SerializedName("total_unread")
        val totalUnread: String? = ""
    ) : Parcelable
}