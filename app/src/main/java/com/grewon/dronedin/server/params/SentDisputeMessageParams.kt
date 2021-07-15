package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class SentDisputeMessageParams(
    var dispute_id: String? = "",
    var msg_type: String? = "",
    @SerializedName("msg")
    var msg: String? = ""
) : Parcelable