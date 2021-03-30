package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class PilotProfileBean(
    @SerializedName("equipment")
    val equipment: List<Equipment?>? = listOf(),
    @SerializedName("portfolio")
    val portfolio: List<Portfolio?>? = listOf(),
    @SerializedName("profile_image")
    val profileImage: String? = "",
    @SerializedName("profile_update")
    val profileUpdate: String? = "",
    @SerializedName("rate")
    val rate: String? = "",
    @SerializedName("review")
    val review: List<String?>? = listOf(),
    @SerializedName("skill")
    val skill: List<Skill?>? = listOf(),
    @SerializedName("total_active_job")
    val totalActiveJob: String? = "",
    @SerializedName("total_complete_job")
    val totalCompleteJob: String? = "",
    @SerializedName("total_earning")
    val totalEarning: String? = "",
    @SerializedName("user_address")
    val userAddress: String? = "",
    @SerializedName("user_bio")
    val userBio: String? = "",
    @SerializedName("user_latitude")
    val userLatitude: String? = "",
    @SerializedName("user_longitude")
    val userLongitude: String? = "",
    @SerializedName("user_name")
    val userName: String? = "",
    @SerializedName("user_type")
    val userType: String? = ""
) : Parcelable {
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
    data class Portfolio(
        @SerializedName("attachment")
        val attachment: List<Attachment?>? = listOf(),
        @SerializedName("description")
        val description: String? = "",
        @SerializedName("portfolio_id")
        val portfolioId: String? = "",
        @SerializedName("title")
        val title: String? = ""
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

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Skill(
        @SerializedName("skill")
        val skill: String? = "",
        @SerializedName("skill_id")
        val skillId: String? = ""
    ) : Parcelable
}