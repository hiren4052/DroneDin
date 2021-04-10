package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class MemberShipBean(
    @SerializedName("data")
    val `data`: ArrayList<Data>? = null,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("package_id")
        val packageId: String? = "",
        @SerializedName("package_name")
        val packageName: String? = "",
        @SerializedName("package_price")
        val packagePrice: String? = "",
        @SerializedName("package_type")
        val packageType: String? = "",
        @SerializedName("package_description")
        val packageDescription: String? = "",
        @SerializedName("package_image")
        val packageImage: String? = ""
    ) : Parcelable
}