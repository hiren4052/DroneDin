package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class UploadAttachmentsParams(
    val filePath: String? = "",
    val attachmentId: String = "",
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable