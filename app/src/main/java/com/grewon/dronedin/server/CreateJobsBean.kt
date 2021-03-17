package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CreateJobsBean(
    @SerializedName("data")
    val `data`: List<Data?>? = listOf(),
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("job_description")
        val jobDescription: String? = "",
        @SerializedName("job_id")
        val jobId: String? = "",
        @SerializedName("job_title")
        val jobTitle: String? = ""
    ) : Parcelable
}