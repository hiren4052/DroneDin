package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class JobInitBean(
    @SerializedName("category")
    val category: ArrayList<Category>? = null,
    @SerializedName("equipment")
    val equipment: ArrayList<Equipment>? = null,
    @SerializedName("skill")
    val skill: ArrayList<Skill>? = null
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Category(
        @SerializedName("category_id")
        val categoryId: String? = "",
        @SerializedName("category_name")
        val categoryName: String? = "",
        var isSelected: Int = 0
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Equipment(
        @SerializedName("equipment")
        val equipment: String? = "",
        @SerializedName("equipment_id")
        val equipmentId: String? = "",
        var isSelected: Int = 0
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Skill(
        @SerializedName("skill")
        val skill: String? = "",
        @SerializedName("skill_id")
        val skillId: String? = "",
        var isSelected: Int = 0
    ) : Parcelable
}