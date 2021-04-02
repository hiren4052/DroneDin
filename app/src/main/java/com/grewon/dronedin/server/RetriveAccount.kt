package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class RetriveAccount(
    @SerializedName("data")
    val `data`: Data? = Data(),
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("dob")
        val dob: String? = "",
        @SerializedName("document")
        val document: String? = "",
        @SerializedName("first_name")
        val firstName: String? = "",
        @SerializedName("is_document_verified")
        val isDocumentVerified: Int? = 0,
        @SerializedName("is_verified")
        val isVerified: Int? = 0,
        @SerializedName("last_name")
        val lastName: String? = ""
    ) : Parcelable
}