package com.grewon.dronedin.server

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class NotificationDataBean(
    @SerializedName("error")
    val error: Boolean? = false,
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("nextOffset")
    val nextOffset: Int? = 0,
    @SerializedName("Result")
    val result: ArrayList<Result>? ,
    @SerializedName("success")
    val success: Boolean? = false
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Result(
        @SerializedName("card_info_id")
        val cardInfoId: Int? = 0,
        @SerializedName("card_info_updatetime")
        val cardInfoUpdatetime: String? = "",
        @SerializedName("user_email")
        val userEmail: String? = "",
        @SerializedName("user_mobile")
        val userMobile: String? = "",
        @SerializedName("user_notification_setting_all_social_media")
        val userNotificationSettingAllSocialMedia: Int? = 0,
        @SerializedName("user_notification_setting_contact")
        val userNotificationSettingContact: Int? = 0,
        @SerializedName("user_notification_setting_datetime")
        val userNotificationSettingDatetime: String? = "",
        @SerializedName("user_notification_setting_email")
        val userNotificationSettingEmail: Int? = 0,
        @SerializedName("user_notification_setting_facebook")
        val userNotificationSettingFacebook: Int? = 0,
        @SerializedName("user_notification_setting_id")
        val userNotificationSettingId: Int? = 0,
        @SerializedName("user_notification_setting_instagram")
        val userNotificationSettingInstagram: Int? = 0,
        @SerializedName("user_notification_setting_linkedin")
        val userNotificationSettingLinkedin: Int? = 0,
        @SerializedName("user_notification_setting_mobile")
        val userNotificationSettingMobile: Int? = 0,
        @SerializedName("user_notification_setting_ref_user_id")
        val userNotificationSettingRefUserId: Int? = 0,
        @SerializedName("user_notification_setting_snapchat")
        val userNotificationSettingSnapchat: Int? = 0,
        @SerializedName("user_notification_setting_soundcloud")
        val userNotificationSettingSoundcloud: Int? = 0,
        @SerializedName("user_notification_setting_tiktok")
        val userNotificationSettingTiktok: Int? = 0,
        @SerializedName("user_notification_setting_twitter")
        val userNotificationSettingTwitter: Int? = 0,
        @SerializedName("user_notification_setting_updatetime")
        val userNotificationSettingUpdatetime: String? = "",
        @SerializedName("user_notification_setting_web")
        val userNotificationSettingWeb: Int? = 0,
        @SerializedName("user_notification_setting_youtube")
        val userNotificationSettingYoutube: Int? = 0,
        @SerializedName("user_profile_bio")
        val userProfileBio: String? = "",
        @SerializedName("user_profile_data_tag")
        val userProfileDataTag: Int? = 0,
        @SerializedName("user_profile_datetime")
        val userProfileDatetime: String? = "",
        @SerializedName("user_profile_facebook")
        val userProfileFacebook: String? = "",
        @SerializedName("user_profile_id")
        val userProfileId: Int? = 0,
        @SerializedName("user_profile_image")
        val userProfileImage: String? = "",
        @SerializedName("user_profile_instagram")
        val userProfileInstagram: String? = "",
        @SerializedName("user_profile_linkedin")
        val userProfileLinkedin: String? = "",
        @SerializedName("user_profile_moj")
        val userProfileMoj: String? = "",
        @SerializedName("user_notification_setting_moj")
        val userNotificationSettingMoj: Int? = 0,
        @SerializedName("user_profile_skype")
        val userProfileSkyPe: String? = "",
        @SerializedName("user_notification_setting_skype")
        val userNotificationSettingSkyPe: Int? = 0,
        @SerializedName("user_profile_paypal")
        val userProfilePayPal: String? = "",
        @SerializedName("user_notification_setting_paypal")
        val userNotificationSettingPayPal: Int? = 0,
        @SerializedName("user_profile_crypto")
        val userProfileCrypto: String? = "",
        @SerializedName("user_notification_setting_crypto")
        val userNotificationSettingCrypto: Int? = 0,
        @SerializedName("user_profile_name")
        val userProfileName: String? = "",
        @SerializedName("user_profile_pin")
        val userProfilePin: String? = "",
        @SerializedName("user_profile_pin_on_off")
        val userProfilePinOnOff: Int? = 0,
        @SerializedName("user_profile_qr_code_image")
        val userProfileQrCodeImage: String? = "",
        @SerializedName("user_profile_qrno")
        val userProfileQrno: String? = "",
        @SerializedName("user_profile_ref_user_id")
        val userProfileRefUserId: Int? = 0,
        @SerializedName("user_profile_snapchat")
        val userProfileSnapchat: String? = "",
        @SerializedName("user_profile_social_data_tag")
        val userProfileSocialDataTag: Int? = 0,
        @SerializedName("user_profile_soundcloud")
        val userProfileSoundcloud: String? = "",
        @SerializedName("user_profile_tiktok")
        val userProfileTiktok: String? = "",
        @SerializedName("user_profile_twitter")
        val userProfileTwitter: String? = "",
        @SerializedName("user_profile_updatedatetime")
        val userProfileUpdatedatetime: String? = "",
        @SerializedName("user_profile_username")
        val userProfileUsername: String? = "",
        @SerializedName("user_profile_web")
        val userProfileWeb: String? = "",
        @SerializedName("user_profile_youtube")
        val userProfileYoutube: String? = ""
    ) : Parcelable
}