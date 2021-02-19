package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class ChatDataBean(
    @SerializedName("error")
    val error: Boolean? = false,
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("nextOffset")
    val nextOffset: Int? = 0,
    @SerializedName("Result")
    val result: ArrayList<Result>?,
    @SerializedName("success")
    val success: Boolean? = false
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Result(
        val messageType: String? = "",
        val sendrId: Long?
    ) : Parcelable
}