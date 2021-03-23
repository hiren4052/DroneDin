package com.grewon.dronedin.server.params

import android.os.Parcelable
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
    var page: String? = ""
) : Parcelable