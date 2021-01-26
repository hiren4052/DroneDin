package com.grewon.dronedin.app


public class AppConstant {
    companion object {

        const val STRIPE_LEGAL: String = " https://stripe.com/en-au/legal"
        val SESSION_BROADCAST: String = "session_broadcast"
        const val NOTIFICATION_BROADCAST: String = "notification_broadcast"
        const val ORDER_BROADCAST: String = "order_broadcast"
        const val ID: String = "id"
        const val UPDATE_REQUEST_CODE: Int = 33
        const val TERMS_OF_SERVICE_URL: String =
            "https://www.termsandcondiitionssample.com/"

        // const val PRIVACY_URL: String = "https://www.chappydelivery.com.au/assets/documents/Chappy-Privacy-Policy-v1.0-FINAL.pdf"
        const val PRIVACY_URL: String = "https://www.chappydelivery.com.au/privacy-policy"
        const val FAQ_URL: String = "https://ubereats-city.appspot.com/en_sg/faq/"
        const val MICROSOFT_URL: String = "https://graph.microsoft.com/v1.0/me"
        val CHANNEL_NAME: CharSequence = "chappy_restaurants"
        const val AUTH_TOKEN: String = "auth_token"
        const val CONSTANT_CREDENTIALS: String = "login_credentials"
        const val REMEMBER_ME: String = "remember_me"
        const val FONT_FAMILY: String = "Raleway-Regular.ttf"
        const val NOTIFICATION_BEAN: String = "notification_bean"
        const val LOCATION_BEAN: String = "location_bean"
        const val CONTACTS_PERMISSION_REQUEST_CODE: Int = 12
        const val ADD_LOCATION_REQUEST_CODE: Int = 10
        const val APP_NAME: String = "Chappy Restaurants"
        const val CHANNEL_ID: String = "info_notification"
        const val LOCATION_PERMISSION_REQUEST_CODE: Int = 15
        const val LOCATION_CLICK_PERMISSION_REQUEST_CODE: Int = 19
        const val DATA_TYPE: String = "data_type"
        const val BEAN: String = "bean"
        const val POSITION: String = "position"
        const val LATITUDE: String = "latitude"
        const val LONGITUDE: String = "longitude"
        const val ADDRESS: String = "address"
        const val USER_BEAN: String = "user_bean"
        private const val BASE_URL = "https://www.app.staging.chappydelivery.com.au/"


        // private const val BASE_URL = "http://socialinfotech.in/development/chappy/"
        const val API_URL = BASE_URL + "api/v2/"
        private const val IMAGE_URL = BASE_URL + "uploads/"
        const val ORIGINAL_IMAGE_URL = IMAGE_URL + "original/"
        const val MEDIUM_IMAGE_URL = IMAGE_URL + "medium/"
        const val THUMB_IMAGE_URL = IMAGE_URL + "thumb/"
        const val TAG: String = "tag"
        const val WEB_URL: String = "web_url"

        const val CAMERA_INTENT_REQUEST_CODE = 44
        const val GALLERY_PERMISSION_REQUEST_CODE = 33
        const val CAMERA_PERMISSION_REQUEST_CODE = 12
        const val GALLERY_INTENT_REQUEST_CODE = 11
        const val CATEGORY_EDIT_REQUEST_CODE = 12
        const val PRODUCT_EDIT_REQUEST_CODE = 13


        /*-------Analytics Constant*/
        const val KEY_DEVICE_NAME = "device"
        const val KEY_USER_ID = "user_id"
        const val KEY_APP_NAME = "app_name"
        const val KEY_API_NAME = "url"
        const val KEY_RESPONSE = "message"

        const val KEY_EVENT_INVALID_RESPONSE = "invalid_response"
        const val KEY_EVENT_EXCEPTION_RESPONSE = "exception_response"

    }
}