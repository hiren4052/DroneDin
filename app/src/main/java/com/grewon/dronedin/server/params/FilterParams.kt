package com.grewon.dronedin.server.params

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FilterParams(
    var category: String? = "",
    var skill: String? = "",
    var equipment: String? = "",
    var location: String? = "",
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0,
    var price: String? = "",
    var page: String? = "",
    var saved: String? = "0",
    @SerializedName("msg")
    val msg: String? = ""

) : Parcelable