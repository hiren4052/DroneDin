package com.grewon.dronedin.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.DroneDinApp
import java.lang.RuntimeException

/**
 * Created by Hiren Gabani on 6/15/20.
 */
class AnalyticsUtils {
    companion object {


        fun setCustomCrashlytics(
            params: String,
            apiUrl: String,
            response: String
        ) {

            FirebaseCrashlytics.getInstance()
                .setCustomKey(AppConstant.KEY_DATE_TIME, TimeUtils.getServerCurrentDateTime())
            FirebaseCrashlytics.getInstance().setCustomKey(AppConstant.KEY_API_NAME, apiUrl)
            FirebaseCrashlytics.getInstance()
                .setCustomKey(AppConstant.AUTH_KEY, DroneDinApp.getAppInstance().getAuthToken())
            FirebaseCrashlytics.getInstance().setCustomKey(AppConstant.KEY_PARAMS, params)
            FirebaseCrashlytics.getInstance().setCustomKey(AppConstant.KEY_RESPONSE, response)
            FirebaseCrashlytics.getInstance().log(response)
            FirebaseCrashlytics.getInstance().recordException(RuntimeException("API Exception"))

        }

    }
}