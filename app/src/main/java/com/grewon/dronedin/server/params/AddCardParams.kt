package com.grewon.dronedin.server.params


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class AddCardParams(
    @SerializedName("card_holder_name")
    val cardHolderName: String? = "",
    @SerializedName("card_number")
    val cardNumber: String? = "",
    @SerializedName("cvv")
    val cvv: String? = "",
    @SerializedName("expire_month")
    val expireMonth: String? = "",
    @SerializedName("expire_year")
    val expireYear: String? = "",
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable