package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CardDataBean(
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("Result")
    val result: List<Result?>?,
    @SerializedName("success")
    val success: Boolean?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Result(
        @SerializedName("user_cardinfo_brand")
        val userCardinfoBrand: String?,
        @SerializedName("user_cardinfo_datetime")
        val userCardinfoDatetime: String?,
        @SerializedName("user_cardinfo_id")
        val userCardinfoId: Int?,
        @SerializedName("user_cardinfo_is_default")
        val userCardinfoIsDefault: String?,
        @SerializedName("user_cardinfo_last4")
        val userCardinfoLast4: Int?,
        @SerializedName("user_cardinfo_ref_user_id")
        val userCardinfoRefUserId: Int?,
        @SerializedName("user_cardinfo_stripe_source")
        val userCardinfoStripeSource: String?
    ) : Parcelable
}