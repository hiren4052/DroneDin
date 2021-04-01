package com.grewon.dronedin.server


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CardDataBean(
    @SerializedName("data")
    val `data`: Data? = Data(),
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("card")
        val card: ArrayList<Card>? = null,
        @SerializedName("default_card")
        val defaultCard: DefaultCard? = DefaultCard()
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Card(
            @SerializedName("billing_details")
            val billingDetails: BillingDetails? = BillingDetails(),
            @SerializedName("card")
            val card: Card? = Card(),
            @SerializedName("created")
            val created: Int? = 0,
            @SerializedName("customer")
            val customer: String? = "",
            @SerializedName("id")
            val id: String? = "",
            @SerializedName("livemode")
            val livemode: Boolean? = false,
            @SerializedName("metadata")
            val metadata: ArrayList<String>? = null,
            @SerializedName("object")
            val objectX: String? = "",
            @SerializedName("type")
            val type: String? = ""
        ) : Parcelable {
            @SuppressLint("ParcelCreator")
            @Parcelize
            data class BillingDetails(
                @SerializedName("address")
                val address: Address? = Address(),
                @SerializedName("email")
                val email: String? = "",
                @SerializedName("name")
                val name: String? = "",
                @SerializedName("phone")
                val phone: String? = ""
            ) : Parcelable {
                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Address(
                    @SerializedName("city")
                    val city: String? = "",
                    @SerializedName("country")
                    val country: String? = "",
                    @SerializedName("line1")
                    val line1: String? = "",
                    @SerializedName("line2")
                    val line2: String? = "",
                    @SerializedName("postal_code")
                    val postalCode: String? = "",
                    @SerializedName("state")
                    val state: String? = ""
                ) : Parcelable
            }

            @SuppressLint("ParcelCreator")
            @Parcelize
            data class Card(
                @SerializedName("brand")
                val brand: String? = "",
                @SerializedName("checks")
                val checks: Checks? = Checks(),
                @SerializedName("country")
                val country: String? = "",
                @SerializedName("exp_month")
                val expMonth: Int? = 0,
                @SerializedName("exp_year")
                val expYear: Int? = 0,
                @SerializedName("fingerprint")
                val fingerprint: String? = "",
                @SerializedName("funding")
                val funding: String? = "",
                @SerializedName("generated_from")
                val generatedFrom: String? = "",
                @SerializedName("last4")
                val last4: String? = "",
                @SerializedName("networks")
                val networks: Networks? = Networks(),
                @SerializedName("three_d_secure_usage")
                val threeDSecureUsage: ThreeDSecureUsage? = ThreeDSecureUsage(),
                @SerializedName("wallet")
                val wallet: String? = ""
            ) : Parcelable {
                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Checks(
                    @SerializedName("address_line1_check")
                    val addressLine1Check: String? = "",
                    @SerializedName("address_postal_code_check")
                    val addressPostalCodeCheck: String? = "",
                    @SerializedName("cvc_check")
                    val cvcCheck: String? = ""
                ) : Parcelable

                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Networks(
                    @SerializedName("available")
                    val available: ArrayList<String>? = null,
                    @SerializedName("preferred")
                    val preferred: String? = ""
                ) : Parcelable

                @SuppressLint("ParcelCreator")
                @Parcelize
                data class ThreeDSecureUsage(
                    @SerializedName("supported")
                    val supported: Boolean? = false
                ) : Parcelable
            }
        }

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class DefaultCard(
            @SerializedName("user_cardinfo_brand")
            val userCardinfoBrand: String? = "",
            @SerializedName("user_cardinfo_datetime")
            val userCardinfoDatetime: String? = "",
            @SerializedName("user_cardinfo_id")
            val userCardinfoId: String? = "",
            @SerializedName("user_cardinfo_is_default")
            val userCardinfoIsDefault: String? = "",
            @SerializedName("user_cardinfo_last4")
            val userCardinfoLast4: String? = "",
            @SerializedName("user_cardinfo_stripe_source")
            val userCardinfoStripeSource: String? = "",
            @SerializedName("user_id")
            val userId: String? = ""
        ) : Parcelable
    }
}