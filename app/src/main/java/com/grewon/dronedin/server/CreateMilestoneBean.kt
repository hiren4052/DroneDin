package com.grewon.dronedin.server

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CreateMilestoneBean(
    val milestoneTitle: String = "",
    val milestonePrice: String = "",
) : Parcelable