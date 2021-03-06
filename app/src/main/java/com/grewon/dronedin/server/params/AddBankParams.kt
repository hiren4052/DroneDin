package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class AddBankParams(
    @SerializedName("acc_holder_firstname")
    var accHolderFirstname: String? = "",
    @SerializedName("acc_holder_lastname")
    var accHolderLastname: String? = "",
    @SerializedName("acc_number")
    var accNumber: String? = "",
    @SerializedName("birth_date")
    var birthDate: String? = "",
    @SerializedName("bsb_number")
    var bsbNumber: String? = "",
    @SerializedName("document")
    var document: String? = "",
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable