package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.server.JobInitBean

@SuppressLint("ParcelCreator")
@Parcelize
data class SubmitProposalParams(
    var job_id: String? = "",
    var proposal_title: String? = "",
    var proposal_description: String? = "",
    var proposal_milestone: String? = "",
    var proposal_total_price: String? = "",
    var milestone: ArrayList<CreateMilestoneBean>? = null,
    var attachments: ArrayList<UploadAttachmentsParams>? = null,
) : Parcelable