package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class PortFolioDataBean(
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
        @SerializedName("job_address")
        val jobAddress: String? = "",
        @SerializedName("job_id")
        val jobId: String? = "",
        @SerializedName("proposal_id")
        val proposalId: String? = "",
        @SerializedName("job_latitude")
        val jobLatitude: String? = "",
        @SerializedName("job_longitude")
        val jobLongitude: String? = "",
        @SerializedName("proposal_datecreated")
        val proposalDatecreated: String? = "",
        @SerializedName("proposal_status")
        val proposalStatus: String? = "",
        @SerializedName("proposal_title")
        val proposalTitle: String? = "",
        @SerializedName("proposal_total_price")
        val proposalTotalPrice: String? = "",
        @SerializedName("user_name")
        val userName: String? = "",
        @SerializedName("user_latitude")
        val userLatitude: String? = "",
        @SerializedName("user_longitude")
        val userLongitude: String? = "",
        @SerializedName("rate")
        val rate: String? = ""
    ) : Parcelable
}