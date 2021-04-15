package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class BioUpdateErrorParams(
    @SerializedName("category_id")
    val categoryId: String? = "",
    @SerializedName("equipment")
    val equipment: String? = "",
    @SerializedName("profile_price")
    val profilePrice: String? = "",
    @SerializedName("skill")
    val skill: String? = "",
    @SerializedName("user_bio")
    val userBio: String? = "",
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable