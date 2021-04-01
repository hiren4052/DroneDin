package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class SentMessageParams(
    var chat_room_id: String? = "",
    var msg_type: String? = "",
    var reciever_id: String? = "",
    var msg: String? = ""
):Parcelable