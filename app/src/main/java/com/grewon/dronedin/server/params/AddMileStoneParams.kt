package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.server.JobInitBean

@SuppressLint("ParcelCreator")
@Parcelize
data class AddMileStoneParams(
    var job_id: String? = "",
    var mileStones: ArrayList<CreateMilestoneBean>? = null,
) : Parcelable