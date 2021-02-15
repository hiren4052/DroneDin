package com.grewon.dronedin.server

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Hiren Gabani on 5/4/20.
 */
@Parcelize
data class LocationBean(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = ""
):Parcelable