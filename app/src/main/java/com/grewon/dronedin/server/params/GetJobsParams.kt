package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class GetJobsParams(
    @SerializedName("job_type")
    val jobType: String? = "",
    @SerializedName("page")
    val page: Int = 1,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable