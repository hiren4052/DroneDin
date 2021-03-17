package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

class IdentificationBean : ArrayList<IdentificationBean.IdentificationBeanItem>() {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class IdentificationBeanItem(
        @SerializedName("proof_id")
        val proofId: String? = "",
        @SerializedName("proof_name")
        val proofName: String? = ""
    ) : Parcelable
}