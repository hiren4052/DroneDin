package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.server.JobInitBean

@SuppressLint("ParcelCreator")
@Parcelize
data class SubmitOfferParams(
    var job_id: String? = "",
    var pilot_id: String? = "",
    var offer_title: String? = "",
    var offer_description: String? = "",
    var offer_milestone: String? = "",
    var offer_total_price: String? = "",
    var milestone: ArrayList<CreateMilestoneBean>? = null,
    var attachments: ArrayList<UploadAttachmentsParams>? = null,
) : Parcelable