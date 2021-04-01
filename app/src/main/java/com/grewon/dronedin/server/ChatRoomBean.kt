package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ChatRoomBean(
    @SerializedName("data")
    val `data`: Data? = Data(),
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("chatroom")
        val chatroom: Chatroom? = Chatroom(),
        @SerializedName("reciever_detail")
        val recieverDetail: RecieverDetail? = RecieverDetail()
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Chatroom(
            @SerializedName("chat_room_datecreated")
            val chatRoomDatecreated: String? = "",
            @SerializedName("chat_room_datetime")
            val chatRoomDatetime: String? = "",
            @SerializedName("chat_room_id")
            val chatRoomId: String? = "",
            @SerializedName("is_disable")
            val isDisable: String? = "",
            @SerializedName("user_id1")
            val userId1: String? = "",
            @SerializedName("user_id2")
            val userId2: String? = ""
        ) : Parcelable

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class RecieverDetail(
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