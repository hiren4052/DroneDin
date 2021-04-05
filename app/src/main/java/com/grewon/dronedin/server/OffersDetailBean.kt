package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class OffersDetailBean(
    @SerializedName("attachment")
    val attachment: ArrayList<Attachment>? = null,
    @SerializedName("category")
    val category: Category? = Category(),
    @SerializedName("date")
    val date: String? = "",
    @SerializedName("job_address")
    val jobAddress: String? = "",
    @SerializedName("job_id")
    val jobId: String? = "",
    @SerializedName("job_latitude")
    val jobLatitude: String? = "",
    @SerializedName("job_longitude")
    val jobLongitude: String? = "",
    @SerializedName("milestone")
    val milestone: ArrayList<Milestone>? = null,
    @SerializedName("offer_description")
    val offerDescription: String? = "",
    @SerializedName("offer_milestone")
    val offerMilestone: String? = "",
    @SerializedName("offer_title")
    val offerTitle: String? = "",
    @SerializedName("offer_total_price")
    val offerTotalPrice: String? = "",
    @SerializedName("user_name")
    val userName: String? = "",
    @SerializedName("client_id")
    val userId: String? = "",
    @SerializedName("pilot")
    val pilot: Pilot? = Pilot()
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Attachment(
        @SerializedName("attachment")
        val attachment: String? = "",
        @SerializedName("attachment_id")
        val attachmentId: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Category(
        @SerializedName("category_id")
        val categoryId: String? = "",
        @SerializedName("category_name")
        val categoryName: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Milestone(
        @SerializedName("milestone_completed_date")
        val milestoneCompletedDate: String? = "",
        @SerializedName("milestone_details")
        val milestoneDetails: String? = "",
        @SerializedName("milestone_id")
        val milestoneId: String? = "",
        @SerializedName("milestone_price")
        val milestonePrice: String? = "",
        @SerializedName("milestone_status")
        val milestoneStatus: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Pilot(
        @SerializedName("user_id")
        val userId: String? = "",
        @SerializedName("user_latitude")
        val userLatitude: String? = "",
        @SerializedName("user_longitude")
        val userLongitude: String? = "",
        @SerializedName("user_name")
        val userName: String? = ""
    ) : Parcelable
}