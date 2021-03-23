package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class PilotDataBean(
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
        @SerializedName("equipment")
        val equipment: String? = "",
        @SerializedName("profile_price")
        val profilePrice: String? = "",
        @SerializedName("rate")
        val rate: String? = "",
        @SerializedName("save_pilot")
        var savePilot: String? = "",
        @SerializedName("skill")
        val skill: String? = "",
        @SerializedName("user_address")
        val userAddress: String? = "",
        @SerializedName("user_id")
        val userId: String? = "",
        @SerializedName("user_latitude")
        val userLatitude: String? = "",
        @SerializedName("user_longitude")
        val userLongitude: String? = "",
        @SerializedName("user_name")
        val userName: String? = "",
        var isSelected: Boolean = false

    ) : Parcelable
}