package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class PilotActiveJobsData(
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
        @SerializedName("job_latitude")
        val jobLatitude: String? = "",
        @SerializedName("job_longitude")
        val jobLongitude: String? = "",
        @SerializedName("offer_datecreated")
        val offerDatecreated: String? = "",
        @SerializedName("offer_id")
        val offerId: String? = "",
        @SerializedName("offer_status")
        val offerStatus: String? = "",
        @SerializedName("offer_status_change_date")
        val offerStatusChangeDate: String? = "",
        @SerializedName("offer_title")
        val offerTitle: String? = "",
        @SerializedName("offer_total_price")
        val offerTotalPrice: String? = "",
        @SerializedName("rate")
        val rate: String? = "",
        @SerializedName("user_name")
        val userName: String? = "",
        @SerializedName("job_title")
        val jobTitle: String? = ""
    ) : Parcelable
}