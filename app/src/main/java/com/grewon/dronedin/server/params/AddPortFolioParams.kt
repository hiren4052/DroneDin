package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.server.JobInitBean

@SuppressLint("ParcelCreator")
@Parcelize
data class AddPortFolioParams(
    var portfolio_title: String? = "",
    var portfolio_desc: String? = "",
    var attachments: ArrayList<UploadAttachmentsParams>? = null,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable