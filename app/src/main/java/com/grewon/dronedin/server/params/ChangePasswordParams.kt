package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ChangePasswordParams(
    @SerializedName("old_password")
    val oldPassword: String? = "",
    @SerializedName("password")
    val password: String? = "",
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable