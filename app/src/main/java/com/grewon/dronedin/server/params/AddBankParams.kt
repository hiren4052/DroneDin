package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class AddBankParams(
    @SerializedName("acc_holder_firstname")
    val accHolderFirstname: String? = "",
    @SerializedName("acc_holder_lastname")
    val accHolderLastname: String? = "",
    @SerializedName("acc_number")
    val accNumber: String? = "",
    @SerializedName("birth_date")
    val birthDate: String? = "",
    @SerializedName("bsb_number")
    val bsbNumber: String? = "",
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable