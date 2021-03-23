package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class PilotJobsDataBean(
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
        @SerializedName("job_datecreated")
        val jobDatecreated: String? = "",
        @SerializedName("job_description")
        val jobDescription: String? = "",
        @SerializedName("job_id")
        val jobId: String? = "",
        @SerializedName("job_latitude")
        val jobLatitude: String? = "",
        @SerializedName("job_longitude")
        val jobLongitude: String? = "",
        @SerializedName("job_title")
        val jobTitle: String? = "",
        @SerializedName("save_job")
        var saveJob: String? = "",
        @SerializedName("total_price")
        val totalPrice: String? = "",
        @SerializedName("user_name")
        val userName: String? = ""
    ) : Parcelable
}