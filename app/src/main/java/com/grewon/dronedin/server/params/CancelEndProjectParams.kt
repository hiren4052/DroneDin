package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CancelEndProjectParams(
    @SerializedName("job_id")
    var jobId: String? = "",
    @SerializedName("request_note")
    var requestNote: String? = "",
    @SerializedName("request_type")
    var requestType: String? = "",
    @SerializedName("msg")
    var msg: String? = ""
) : Parcelable