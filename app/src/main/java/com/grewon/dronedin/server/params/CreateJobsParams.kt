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
    var selectedCategoryId: String? = "",
    var categoryList: ArrayList<JobInitBean.Category>? = null,
    var equipmentsList: ArrayList<JobInitBean.Equipment>? = null,
    var selectedEquipmentsIdList: ArrayList<String>? = null,
    var skillList: ArrayList<JobInitBean.Skill>? = null,
    var selectedSkillsIdList: ArrayList<String>? = null,
    var jobTitle: String? = "",
    var jobDescription: String? = "",
    var jobAddress: String? = "",
    var jobLatitude: Double? = 0.0,
    var jobLongitude: Double? = 0.0,
    var jobTotalPrice: String? = "",
    var mileStones: ArrayList<CreateMilestoneBean>? = null,
    var attachments: ArrayList<UploadAttachmentsParams>? = null,
) : Parcelable