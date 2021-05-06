package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.server.JobInitBean

@SuppressLint("ParcelCreator")
@Parcelize
data class SubmitMilestoneParams(
    var milestone_id: String? = "",
    var milestone_request_note: String? = "",
    var attachments: ArrayList<UploadAttachmentsParams>? = null,
    var job_id: String? = ""
) : Parcelable