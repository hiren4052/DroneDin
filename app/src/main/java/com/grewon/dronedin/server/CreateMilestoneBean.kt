package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class CreateMilestoneBean(
    val details: String = "",
    val price: String = "",
    val milestoneId: String = "",
) : Parcelable