package com.grewon.dronedin.server

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class DisputeBean(
    @SerializedName("data")
    val `data`: ArrayList<Data>? = null,
    @SerializedName("msg")
    val msg: String? = null
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("dispute_id")
        val disputeId: String? = null,
        @SerializedName("dispute_reason")
        val disputeReason: String? = null,
        @SerializedName("dispute_status")
        val disputeStatus: String? = null,
        @SerializedName("last_message")
        val lastMessage: String? = null,
        @SerializedName("last_message_ext")
        val lastMessageExt: String? = null,
        @SerializedName("last_message_type")
        val lastMessageType: String? = null,
        @SerializedName("last_update")
        val lastUpdate: String? = null,
        @SerializedName("sender_id")
        val senderId: String? = null,
        @SerializedName("sender_type")
        val senderType: String? = null,
        @SerializedName("total_unread")
        val totalUnread: String? = null,
        @SerializedName("user_details")
        val userDetails: UserDetails? = null
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class UserDetails(
            @SerializedName("is_user_online")
            val isUserOnline: String? = null,
            @SerializedName("profile_image")
            val profileImage: String? = null,
            @SerializedName("user_id")
            val userId: String? = null,
            @SerializedName("user_name")
            val userName: String? = null
        ) : Parcelable
    }
}