package com.app.locationapp.server

import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("error")
    val error: Boolean = false,
    @SerializedName("message")
    val message: String = ""
)