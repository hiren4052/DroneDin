package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class CancelEndProjectStatusUpdateParams(
    @SerializedName("job_cancel_end_request_id")
    var jobCancelEndRequestId: String? = "",
    @SerializedName("request_reject_reason")
    var requestRejectReason: String? = "",
    @SerializedName("request_status")
    var requestStatus: String? = "",
    @SerializedName("request_type")
    var requestType: String? = ""
) : Parcelable