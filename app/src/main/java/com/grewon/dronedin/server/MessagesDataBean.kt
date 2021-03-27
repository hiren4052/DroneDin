package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class MessagesDataBean(
    @SerializedName("data")
    val `data`: ArrayList<Data>? = null,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("chat_room_datecreated")
        val chatRoomDatecreated: String? = "",
        @SerializedName("chat_room_datetime")
        val chatRoomDatetime: String? = "",
        @SerializedName("chat_room_id")
        val chatRoomId: String? = "",
        @SerializedName("is_disable")
        val isDisable: String? = "",
        @SerializedName("last_message")
        val lastMessage: String? = "",
        @SerializedName("last_message_ext")
        val lastMessageExt: String? = "",
        @SerializedName("last_message_type")
        val lastMessageType: String? = "",
        @SerializedName("last_update")
        val lastUpdate: String? = "",
        @SerializedName("total_unread")
        val totalUnread: String? = "",
        @SerializedName("user_details")
        val userDetails: UserDetails? = UserDetails(),
        @SerializedName("user_id1")
        val userId1: String? = "",
        @SerializedName("user_id2")
        val userId2: String? = ""
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class UserDetails(
            @SerializedName("is_user_online")
            val isUserOnline: String? = "",
            @SerializedName("profile_image")
            val profileImage: String? = "",
            @SerializedName("user_id")
            val userId: String? = "",
            @SerializedName("user_name")
            val userName: String? = ""
        ) : Parcelable
    }
}