package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class PilotInviteParams(
    @SerializedName("job_id")
    val jobId: String? = "",
    @SerializedName("pilot_ids")
    val pilotIds: List<Int>? = null,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable