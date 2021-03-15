package com.grewon.dronedin.server
import com.google.gson.annotations.SerializedName


/**
 * Created by Jeff Klima on 2019-08-31.
 */
data class FacebookModel(
    @SerializedName("email")
    val email: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("picture")
    val picture: Picture?
) {
    data class Picture(
        @SerializedName("data")
        val `data`: Data
    ) {
        data class Data(
            @SerializedName("height")
            val height: Int,
            @SerializedName("is_silhouette")
            val isSilhouette: Boolean,
            @SerializedName("url")
            val url: String,
            @SerializedName("width")
            val width: Int
        )
    }
}