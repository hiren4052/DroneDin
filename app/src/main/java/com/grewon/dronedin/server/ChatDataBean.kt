package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class ChatDataBean(
    @SerializedName("data")
    val `data`: ArrayList<Data>? = null,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("chat_msg_datecreated")
        var chatMsgDatecreated: String? = "",
        @SerializedName("chat_msg_id")
        var chatMsgId: String? = "",
        @SerializedName("extension")
        var extension: String? = "",
        @SerializedName("is_read")
        var isRead: String? = "",
        @SerializedName("msg")
        var msg: String? = "",
        @SerializedName("msg_type")
        var msgType: String? = "",
        @SerializedName("reciever_id")
        var recieverId: String? = "",
        @SerializedName("sender_id")
        var senderId: String? = ""
    ) : Parcelable
}