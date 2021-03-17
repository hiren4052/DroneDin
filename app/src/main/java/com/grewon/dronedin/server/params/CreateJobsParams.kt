package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.server.JobInitBean

@SuppressLint("ParcelCreator")
@Parcelize
data class CreateJobsParams(
    val selectedCategoryId: String? = "",
    val categoryList: List<JobInitBean.Category>? = null,
    val equipmentsList: ArrayList<JobInitBean.Equipment>? = null,
    val skillList: List<JobInitBean.Skill>? = null,
    val jobTitle: String? = "",
    val jobDescription: String? = "",
    val jobAddress: String? = "",
    val jobLatitude: Double? ,
    val jobLongitude: Double? ,
    val jobTotalPrice: String? = "",
    val mileStones: ArrayList<CreateMilestoneBean>? = null,
    val attachments: ArrayList<CreateMilestoneBean>? = null,
) : Parcelable