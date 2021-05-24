package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class ProfileBean(
    @SerializedName("data")
    val `data`: Data? = Data(),
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("proof")
        val proof: Proof? = Proof(),
        @SerializedName("proof_back_side")
        val proofBackSide: String? = "",
        @SerializedName("proof_front_side")
        val proofFrontSide: String? = "",
        @SerializedName("user_address")
        val userAddress: String? = "",
        @SerializedName("user_email")
        val userEmail: String? = "",
        @SerializedName("user_latitude")
        val userLatitude: String? = "",
        @SerializedName("user_longitude")
        val userLongitude: String? = "",
        @SerializedName("user_name")
        val userName: String? = "",
        @SerializedName("user_phone_number")
        val userPhoneNumber: String? = "",
        @SerializedName("profile_image")
        val profileImage: String? = "",
        @SerializedName("document_id")
        val documentId: String? = ""
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Proof(
            @SerializedName("proof_id")
            val proofId: String? = "",
            @SerializedName("proof_name")
            val proofName: String? = ""
        ) : Parcelable
    }
}