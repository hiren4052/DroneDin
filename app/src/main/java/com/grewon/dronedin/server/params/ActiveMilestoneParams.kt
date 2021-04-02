package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ActiveMilestoneParams(
    @SerializedName("milestone_id")
    val milestoneId: String? = "",
    @SerializedName("user_wallet")
    val userWallet: String? = ""
) : Parcelable