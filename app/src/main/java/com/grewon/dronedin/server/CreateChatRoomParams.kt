package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


data class CreateChatRoomParams(
    @SerializedName("reciever_id")
    val recieverId: String? = ""
)