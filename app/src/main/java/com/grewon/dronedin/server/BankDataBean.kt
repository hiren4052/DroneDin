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
    @SerializedName("bank_detail")
    val bankDetail: BankDetail? = BankDetail(),
    @SerializedName("msg")
    val msg: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class BankDetail(
        @SerializedName("bank")
        val bank: ArrayList<Bank>? = null,
        @SerializedName("default_bank")
        val defaultBank: DefaultBank? = DefaultBank()
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Bank(
            @SerializedName("account")
            val account: String? = "",
            @SerializedName("account_holder_name")
            val accountHolderName: String? = "",
            @SerializedName("account_holder_type")
            val accountHolderType: String? = "",
            @SerializedName("available_payout_methods")
            val availablePayoutMethods: ArrayList<String>? = null,
            @SerializedName("bank_name")
            val bankName: String? = "",
            @SerializedName("country")
            val country: String? = "",
            @SerializedName("currency")
            val currency: String? = "",
            @SerializedName("default_for_currency")
            val defaultForCurrency: Boolean? = false,
            @SerializedName("fingerprint")
            val fingerprint: String? = "",
            @SerializedName("id")
            val id: String? = "",
            @SerializedName("last4")
            val last4: String? = "",
            @SerializedName("metadata")
            val metadata: ArrayList<String>? = null,
            @SerializedName("object")
            val objectX: String? = "",
            @SerializedName("routing_number")
            val routingNumber: String? = "",
            @SerializedName("status")
            val status: String? = ""
        ) : Parcelable

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class DefaultBank(
            @SerializedName("user_bank_account_bank_id")
            val userBankAccountBankId: String? = "",
            @SerializedName("user_bank_account_bank_token")
            val userBankAccountBankToken: String? = "",
            @SerializedName("user_bank_account_datetime")
            val userBankAccountDatetime: String? = "",
            @SerializedName("user_bank_account_id")
            val userBankAccountId: String? = "",
            @SerializedName("user_bank_account_is_default")
            val userBankAccountIsDefault: String? = "",
            @SerializedName("user_bank_account_stripe_account_id")
            val userBankAccountStripeAccountId: String? = "",
            @SerializedName("user_id")
            val userId: String? = ""
        ) : Parcelable
    }
}