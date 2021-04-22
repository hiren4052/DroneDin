package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class CreateMilestoneBean(
    var details: String = "",
    var price: String = "",
    var milestoneId: String = "",
) : Parcelable