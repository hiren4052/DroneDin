package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class ActiveJobsDetails(
    @SerializedName("attachment")
    val attachment: ArrayList<Attachment>? = null,
    @SerializedName("cancel_complete_request_milestone")
    val cancelCompleteRequestMilestone: CancelCompleteRequestMilestone? = null,
    @SerializedName("cancel_job_request")
    val cancelJobRequest: CancelJobRequest? = null,
    @SerializedName("category")
    val category: Category? = Category(),
    @SerializedName("date")
    val date: String? = "",
    @SerializedName("equipment")
    val equipment: ArrayList<Equipment>? = null,
    @SerializedName("job_address")
    val jobAddress: String? = "",
    @SerializedName("job_description")
    val jobDescription: String? = "",
    @SerializedName("offer_description")
    val offerDescription: String? = "",
    @SerializedName("job_id")
    val jobId: String? = "",
    @SerializedName("job_latitude")
    val jobLatitude: String? = "",
    @SerializedName("job_longitude")
    val jobLongitude: String? = "",
    @SerializedName("job_title")
    val jobTitle: String? = "",
    @SerializedName("offer_status")
    val offerStatus: String? = "",
    @SerializedName("offer_title")
    val offerTitle: String? = "",
    @SerializedName("milestone")
    val milestone: ArrayList<Milestone>? = null,
    @SerializedName("offer_milestone")
    val offerMilestone: String? = "",
    @SerializedName("offer_total_price")
    val offerTotalPrice: String? = "",
    @SerializedName("pilot")
    val pilot: Pilot? = Pilot(),
    @SerializedName("requested_milestone")
    val requestedMilestone: RequestedMilestone? = null,
    @SerializedName("skill")
    val skill: ArrayList<Skill>? = null,
    @SerializedName("user_name")
    val userName: String? = "",
    @SerializedName("client_id")
    val userId: String? = ""

) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Attachment(
        @SerializedName("attachment")
        val attachment: String? = "",
        @SerializedName("attachment_id")
        val attachmentId: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class CancelCompleteRequestMilestone(
        @SerializedName("milestone_datecreated")
        val milestoneDatecreated: String? = "",
        @SerializedName("msg")
        val msg: String? = "",
        @SerializedName("milestone_id")
        val milestoneId: String? = "",
        @SerializedName("milestone_request_id")
        val milestoneRequestId: String? = "",
        @SerializedName("milestone_request_type")
        val milestoneRequestType: String? = "",
        @SerializedName("milestone_request_status")
        val milestoneRequestStatus: String? = "",
        @SerializedName("sender_id")
        val senderId: String? = "",
        @SerializedName("reciever_id")
        val recieverId: String? = "",
        @SerializedName("milestone_request_reject_reason")
        val milestoneRequestRejectReason: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Category(
        @SerializedName("category_id")
        val categoryId: String? = "",
        @SerializedName("category_name")
        val categoryName: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Equipment(
        @SerializedName("equipment")
        val equipment: String? = "",
        @SerializedName("equipment_id")
        val equipmentId: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class CancelJobRequest(
        @SerializedName("job_cancel_end_request_id")
        val jobCancelEndRequestId: String? = "",
        @SerializedName("job_id")
        val jobId: String? = "",
        @SerializedName("msg")
        val msg: String? = "",
        @SerializedName("reciever_id")
        val recieverId: String? = "",
        @SerializedName("request_note")
        val requestNote: String? = "",
        @SerializedName("request_reject_reason")
        val requestRejectReason: String? = "",
        @SerializedName("request_status")
        val requestStatus: String? = "",
        @SerializedName("request_type")
        val requestType: String? = "",
        @SerializedName("sender_id")
        val senderId: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Milestone(
        @SerializedName("milestone_cancelled_date")
        var milestoneCancelledDate: String? = "",
        @SerializedName("milestone_completed_date")
        var milestoneCompletedDate: String? = "",
        @SerializedName("milestone_details")
        var milestoneDetails: String? = "",
        @SerializedName("milestone_id")
        var milestoneId: String? = "",
        @SerializedName("milestone_price")
        var milestonePrice: String? = "",
        @SerializedName("milestone_request_note")
        var milestoneRequestNote: String? = "",
        @SerializedName("milestone_started_date")
        var milestoneStartedDate: String? = "",
        @SerializedName("milestone_status")
        var milestoneStatus: String? = "",
        @SerializedName("milestone_request_id")
        var milestoneRequestId: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Pilot(
        @SerializedName("user_id")
        val userId: String? = "",
        @SerializedName("user_latitude")
        val userLatitude: String? = "",
        @SerializedName("user_longitude")
        val userLongitude: String? = "",
        @SerializedName("user_name")
        val userName: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class RequestedMilestone(
        @SerializedName("milestone_datecreated")
        val milestoneDatecreated: String? = "",
        @SerializedName("msg")
        val msg: String? = "",
        @SerializedName("milestone_id")
        val milestoneId: String? = "",
        @SerializedName("milestone_request_id")
        val milestoneRequestId: String? = "",
        @SerializedName("milestone_request_type")
        val milestoneRequestType: String? = "",
        @SerializedName("milestone_request_status")
        val milestoneRequestStatus: String? = "",
        @SerializedName("milestone_request_reject_reason")
        val milestoneRequestRejectReason: String? = ""
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Skill(
        @SerializedName("skill")
        val skill: String? = "",
        @SerializedName("skill_id")
        val skillId: String? = ""
    ) : Parcelable
}