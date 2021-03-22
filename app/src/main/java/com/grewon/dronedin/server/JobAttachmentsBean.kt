package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize



@SuppressLint("ParcelCreator")
@Parcelize
data class JobAttachmentsBean(
    @SerializedName("attachment")
    var attachment: String? = "",
    @SerializedName("attachment_id")
    var attachmentId: String? = ""
) : Parcelable