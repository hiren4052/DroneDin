package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class ProfileBioDataBean(
    @SerializedName("data")
    val `data`: Data? = Data(),
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("category")
        val category: Category? = Category(),
        @SerializedName("equipment")
        val equipment: ArrayList<Equipment>? = null,
        @SerializedName("profile_price")
        val profilePrice: String? = "",
        @SerializedName("skill")
        val skill: ArrayList<Skill>? = null,
        @SerializedName("user_bio")
        val userBio: String? = "",
        @SerializedName("user_id")
        val userId: String? = ""
    ) : Parcelable {
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
        data class Skill(
            @SerializedName("skill")
            val skill: String? = "",
            @SerializedName("skill_id")
            val skillId: String? = ""
        ) : Parcelable
    }
}