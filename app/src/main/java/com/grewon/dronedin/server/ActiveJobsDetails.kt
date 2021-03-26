package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ActiveJobsDetails(
    @SerializedName("attachment")
    val attachment: ArrayList<Attachment>? = null,
    @SerializedName("category")
    val category: Category? = Category(),
    @SerializedName("pilot")
    val pilot: Pilot? = Pilot(),
    @SerializedName("date")
    val date: String? = "",
    @SerializedName("equipment")
    val equipment: ArrayList<Equipment>? = null,
    @SerializedName("job_address")
    val jobAddress: String? = "",
    @SerializedName("job_id")
    val jobId: String? = "",
    @SerializedName("job_latitude")
    val jobLatitude: String? = "",
    @SerializedName("job_longitude")
    val jobLongitude: String? = "",
    @SerializedName("job_description")
    val jobDescription: String? = "",
    @SerializedName("job_title")
    val jobTitle: String? = "",
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
    @SerializedName("skill")
    val skill: ArrayList<Skill>? = null,
    @SerializedName("user_name")
    val userName: String? = ""
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
    data class Equipment(
        @SerializedName("equipment")
        val equipment: String? = "",
        @SerializedName("equipment_id")
        val equipmentId: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Milestone(
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

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Skill(
        @SerializedName("skill")
        val skill: String? = "",
        @SerializedName("skill_id")
        val skillId: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Pilot(
        @SerializedName("user_id")
        val userId: String? = "",
        @SerializedName("user_name")
        val userName: String? = "",
        @SerializedName("user_latitude")
        val userLatitude: String? = "",
        @SerializedName("user_longitude")
        val userLongitude: String? = ""
    ) : Parcelable
}