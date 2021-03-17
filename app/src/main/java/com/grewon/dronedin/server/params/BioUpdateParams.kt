package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class BioUpdateParams(
    @SerializedName("category_id")
    val categoryId: String? = "",
    @SerializedName("equipment")
    val equipment: List<Int?>? = null,
    @SerializedName("profile_price")
    val profilePrice: String? = "",
    @SerializedName("skill")
    val skill: List<Int>? = null,
    @SerializedName("user_bio")
    val userBio: String? = ""
) : Parcelable