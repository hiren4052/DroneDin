package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class SentDisputeChatBean(
    @SerializedName("data")
    val `data`: Data? = Data(),
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("chat_msg_datecreated")
        val chatMsgDatecreated: String? = "",
        @SerializedName("chat_msg_id")
        val chatMsgId: String? = "",
        @SerializedName("extension")
        val extension: String? = "",
        @SerializedName("is_read")
        val isRead: String? = "",
        @SerializedName("msg")
        val msg: String? = "",
        @SerializedName("msg_type")
        val msgType: String? = "",
        @SerializedName("reciever_id")
        val recieverId: String? = "",
        @SerializedName("sender_id")
        val senderId: String? = ""
    ) : Parcelable
}