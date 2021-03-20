package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize



@SuppressLint("ParcelCreator")
@Parcelize
data class JobsDataBean(
    @SerializedName("data")
    val `data`: ArrayList<Data>? = null,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("category_name")
        val categoryName: String? = "",
        @SerializedName("job_id")
        val jobId: String? = "",
        @SerializedName("job_title")
        val jobTitle: String? = "",
        @SerializedName("total_invitation")
        val totalInvitation: String? = "",
        @SerializedName("total_price")
        val totalPrice: String? = "",
        @SerializedName("total_proposal")
        val totalProposal: String? = ""
    ) : Parcelable
}