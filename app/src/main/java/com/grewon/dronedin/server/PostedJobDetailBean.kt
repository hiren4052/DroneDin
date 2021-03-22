package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class PostedJobDetailBean(
    @SerializedName("attachment")
    val attachment: ArrayList<Attachment>? = null,
    @SerializedName("category")
    val category: Category? = Category(),
    @SerializedName("date")
    val date: String? = "",
    @SerializedName("equipment")
    val equipment: ArrayList<Equipment>? = null,
    @SerializedName("job_address")
    val jobAddress: String? = "",
    @SerializedName("job_description")
    val jobDescription: String? = "",
    @SerializedName("job_latitude")
    val jobLatitude: String? = "",
    @SerializedName("job_longitude")
    val jobLongitude: String? = "",
    @SerializedName("job_title")
    val jobTitle: String? = "",
    @SerializedName("milestone")
    val milestone: ArrayList<Milestone>? = null,
    @SerializedName("skill")
    val skill: ArrayList<Skill>? = null,
    @SerializedName("total_price")
    val totalPrice: String? = "",
    @SerializedName("total_proposal")
    val totalProposal: String? = ""
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
        @SerializedName("milestone_details")
        val milestoneDetails: String? = "",
        @SerializedName("milestone_id")
        val milestoneId: String? = "",
        @SerializedName("milestone_price")
        val milestonePrice: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Skill(
        @SerializedName("skill")
        val skill: String? = "",
        @SerializedName("skill_id")
        val skillId: String? = ""
    ) : Parcelable
}