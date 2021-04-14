package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize




@SuppressLint("ParcelCreator")
@Parcelize
data class DisputeChatDataBean(
    @SerializedName("data")
    var `data`: ArrayList<Data>? = null,
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
        @SerializedName("msg")
        var msg: String? = "",
        @SerializedName("msg_type")
        var msgType: String? = "",
        @SerializedName("sender_id")
        var senderId: String? = "",
        @SerializedName("sender_type")
        var senderType: String? = ""
    ) : Parcelable
}