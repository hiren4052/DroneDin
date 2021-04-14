package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class SentDisputeChatBean(
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("msg")
    var msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("extension")
        var extension: String? = "",
        @SerializedName("group_chat_msg_datecreated")
        var groupChatMsgDatecreated: String? = "",
        @SerializedName("group_chat_msg_id")
        var groupChatMsgId: String? = "",
        @SerializedName("is_read")
        var isRead: String? = "",
        @SerializedName("is_user_online")
        var isUserOnline: String? = "",
        @SerializedName("msg")
        var msg: String? = "",
        @SerializedName("msg_type")
        var msgType: String? = "",
        @SerializedName("profile_image")
        var profileImage: String? = "",
        @SerializedName("sender_id")
        var senderId: String? = "",
        @SerializedName("user_name")
        var userName: String? = ""
    ) : Parcelable
}