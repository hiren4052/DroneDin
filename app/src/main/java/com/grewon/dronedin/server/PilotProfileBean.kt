package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable



@SuppressLint("ParcelCreator")
@Parcelize
data class PilotProfileBean(
    @SerializedName("equipment")
    val equipment: ArrayList<Equipment>? = null,
    @SerializedName("portfolio")
    val portfolio: ArrayList<Portfolio>? = null,
    @SerializedName("profile_image")
    val profileImage: String? = null,
    @SerializedName("profile_update")
    val profileUpdate: String? = null,
    @SerializedName("rate")
    val rate: String? = null,
    @SerializedName("review")
    val review: ArrayList<ReviewsDataBean>? = null,
    @SerializedName("skill")
    val skill: ArrayList<Skill>? = null,
    @SerializedName("total_active_job")
    val totalActiveJob: String? = null,
    @SerializedName("total_complete_job")
    val totalCompleteJob: String? = null,
    @SerializedName("total_earning")
    val totalEarning: String? = null,
    @SerializedName("user_address")
    val userAddress: String? = null,
    @SerializedName("user_bio")
    val userBio: String? = null,
    @SerializedName("user_latitude")
    val userLatitude: String? = null,
    @SerializedName("user_longitude")
    val userLongitude: String? = null,
    @SerializedName("user_name")
    val userName: String? = null,
    @SerializedName("user_type")
    val userType: String? = null
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Equipment(
        @SerializedName("equipment")
        val equipment: String? = null,
        @SerializedName("equipment_id")
        val equipmentId: String? = null
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Portfolio(
        @SerializedName("attachment")
        val attachment: List<Attachment?>? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("portfolio_id")
        val portfolioId: String? = null,
        @SerializedName("title")
        val title: String? = null
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Attachment(
            @SerializedName("attachment")
            val attachment: String? = null,
            @SerializedName("attachment_id")
            val attachmentId: String? = null
        ) : Parcelable
    }



    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Skill(
        @SerializedName("skill")
        val skill: String? = null,
        @SerializedName("skill_id")
        val skillId: String? = null
    ) : Parcelable
}