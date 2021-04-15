package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class EarningsDataBean(
    @SerializedName("data")
    val `data`: ArrayList<Data>? = null,
    @SerializedName("msg")
    val msg: String? = "",
    @SerializedName("total_earning")
    val totalEarning: String? = "",
    @SerializedName("wallet_balance")
    val walletBalance: String? = "",
    @SerializedName("total_withdraw")
    val totalWithdraw: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("job_id")
        val jobId: String? = "",
        @SerializedName("milestone_id")
        val milestoneId: String? = "",
        @SerializedName("msg")
        val msg: String? = "",
        @SerializedName("offer_id")
        val offerId: String? = "",
        @SerializedName("transaction_msg")
        val transactionMsg: String? = "",
        @SerializedName("transaction_status")
        val transactionStatus: String? = "",
        @SerializedName("transaction_title")
        val transactionTitle: String? = "",
        @SerializedName("transition_type")
        val transitionType: String? = "",
        @SerializedName("user_name")
        val userName: String? = "",
        @SerializedName("wallet_statement_datecreated")
        val walletStatementDatecreated: String? = "",
        @SerializedName("wallet_statement_id")
        val walletStatementId: String? = "",
        @SerializedName("wallet_transaction_amt")
        val walletTransactionAmt: String? = "",
        var isLoading: Boolean = false
    ) : Parcelable
}