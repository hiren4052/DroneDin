package com.grewon.dronedin.server

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize


/**
 * Created by Hiren Gabani on 6/19/20.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class BankDataBean(
    @SerializedName("default_card")
    val defaultCard: String?,
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("Result")
    val result: Result?,
    @SerializedName("success")
    val success: Boolean?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Result(
        @SerializedName("data")
        val `data`: ArrayList<Data>?,
        @SerializedName("has_more")
        val hasMore: Boolean?,
        @SerializedName("object")
        val objectX: String?,
        @SerializedName("url")
        val url: String?
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Data(
            @SerializedName("billing_details")
            val billingDetails: BillingDetails?,
            @SerializedName("card")
            val card: Card?,
            @SerializedName("created")
            val created: Int?,
            @SerializedName("customer")
            val customer: String?,
            @SerializedName("id")
            val id: String?,
            @SerializedName("livemode")
            val livemode: Boolean?,
            @SerializedName("metadata")
            val metadata: List<String?>?,
            @SerializedName("object")
            val objectX: String?,
            @SerializedName("type")
            val type: String?
        ) : Parcelable {
            @SuppressLint("ParcelCreator")
            @Parcelize
            data class BillingDetails(
                @SerializedName("address")
                val address: Address?,
                @SerializedName("email")
                val email: String?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("phone")
                val phone: String?
            ) : Parcelable {
                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Address(
                    @SerializedName("city")
                    val city: String?,
                    @SerializedName("country")
                    val country: String?,
                    @SerializedName("line1")
                    val line1: String?,
                    @SerializedName("line2")
                    val line2: String?,
                    @SerializedName("postal_code")
                    val postalCode: String?,
                    @SerializedName("state")
                    val state: String?
                ) : Parcelable
            }

            @SuppressLint("ParcelCreator")
            @Parcelize
            data class Card(
                @SerializedName("brand")
                val brand: String?,
                @SerializedName("checks")
                val checks: Checks?,
                @SerializedName("country")
                val country: String?,
                @SerializedName("exp_month")
                val expMonth: Int?,
                @SerializedName("exp_year")
                val expYear: Int?,
                @SerializedName("fingerprint")
                val fingerprint: String?,
                @SerializedName("funding")
                val funding: String?,
                @SerializedName("generated_from")
                val generatedFrom: String?,
                @SerializedName("last4")
                val last4: String?,
                @SerializedName("networks")
                val networks: Networks?,
                @SerializedName("three_d_secure_usage")
                val threeDSecureUsage: ThreeDSecureUsage?,
                @SerializedName("wallet")
                val wallet: String?
            ) : Parcelable {
                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Checks(
                    @SerializedName("address_line1_check")
                    val addressLine1Check: String?,
                    @SerializedName("address_postal_code_check")
                    val addressPostalCodeCheck: String?,
                    @SerializedName("cvc_check")
                    val cvcCheck: String?
                ) : Parcelable

                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Networks(
                    @SerializedName("available")
                    val available: List<String?>?,
                    @SerializedName("preferred")
                    val preferred: String?
                ) : Parcelable

                @SuppressLint("ParcelCreator")
                @Parcelize
                data class ThreeDSecureUsage(
                    @SerializedName("supported")
                    val supported: Boolean?
                ) : Parcelable
            }
        }
    }
}