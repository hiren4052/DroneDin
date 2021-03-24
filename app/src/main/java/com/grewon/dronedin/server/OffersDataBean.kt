package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class OffersDataBean(
    @SerializedName("data")
    val `data`: ArrayList<Data>? = null,
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("category_name")
        val categoryName: String? = "",
        @SerializedName("job_address")
        val jobAddress: String? = "",
        @SerializedName("offer_datecreated")
        val offerDatecreated: String? = "",
        @SerializedName("offer_description")
        val offerDescription: String? = "",
        @SerializedName("offer_id")
        val offerId: String? = "",
        @SerializedName("offer_title")
        val offerTitle: String? = "",
        @SerializedName("offer_total_price")
        val offerTotalPrice: String? = "",
        @SerializedName("user_address")
        val userAddress: String? = "",
        @SerializedName("user_latitude")
        val userLatitude: String? = "",
        @SerializedName("user_longitude")
        val userLongitude: String? = "",
        @SerializedName("job_latitude")
        val jobLatitude: String? = "",
        @SerializedName("job_longitude")
        val jobLongitude: String? = "",
        @SerializedName("user_name")
        val userName: String? = ""
    ) : Parcelable
}