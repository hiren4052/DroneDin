package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class MainScreenData(
    @SerializedName("data")
    val `data`: Data? = Data(),
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("badge")
        val badge: String? = "",
        @SerializedName("bank_verified")
        val bankVerified: String? = "",
        @SerializedName("proof_front_side")
        val proofFrontSide: String? = "",
        @SerializedName("proof_back_side")
        val proofBackSide: String? = "",
        @SerializedName("document_verified")
        val documentVerified: String? = "",
        @SerializedName("package_type")
        val packageType: String? = "",
        @SerializedName("privacy_policy_url")
        val privacyPolicyUrl: String? = "",
        @SerializedName("profile_image")
        val profileImage: String? = "",
        @SerializedName("profile_price")
        val profilePrice: String? = "",
        @SerializedName("profile_update")
        val profileUpdate: String? = "",
        @SerializedName("stripe_commission")
        val stripeCommission: String? = "",
        @SerializedName("terms_conditions_url")
        val termsConditionsUrl: String? = "",
        @SerializedName("customer_support_url")
        val customerSupportUrl: String? = "",
        @SerializedName("total_unread_msg")
        val totalUnreadMsg: String? = "",
        @SerializedName("total_unread_notification")
        val totalUnreadNotification: String? = "",
        @SerializedName("user_address")
        val userAddress: String? = "",
        @SerializedName("notification")
        val notification: String? = "",
        @SerializedName("user_email")
        val userEmail: String? = "",
        @SerializedName("user_fcm_token")
        val userFcmToken: String? = "",
        @SerializedName("user_latitude")
        val userLatitude: String? = "",
        @SerializedName("user_longitude")
        val userLongitude: String? = "",
        @SerializedName("user_name")
        val userName: String? = "",
        @SerializedName("user_phone_number")
        val userPhoneNumber: String? = "",
        @SerializedName("user_verified")
        val userVerified: String? = "",
        @SerializedName("user_wallet")
        val userWallet: String? = "",
        @SerializedName("user_warning_text")
        val userWarningText: String? = ""
    ) : Parcelable
}