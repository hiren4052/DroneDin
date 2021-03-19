package com.grewon.dronedin.server.params


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class UserIdParams(
    @SerializedName("user_id")
    val userId: String? = ""
) : Parcelable