package com.grewon.dronedin.utils

import android.content.SharedPreferences
import com.grewon.dronedin.server.UserData
import com.google.gson.Gson
import com.grewon.dronedin.app.AppConstant


/**
 * Created by Jeff Klima on 2019-08-26.
 */
class PreferenceUtils(private val sharedPreferences: SharedPreferences) {


    fun saveAuthToken(string: String) {

        sharedPreferences.edit().putString(AppConstant.AUTH_TOKEN, string).apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(AppConstant.AUTH_TOKEN, "")
    }

    fun flushData() {
        sharedPreferences.edit().clear().apply()
    }


    fun saveLoginCredential(response: UserData) {
        val json = Gson().toJson(response)
        sharedPreferences.edit().putString(AppConstant.CONSTANT_CREDENTIALS, json).apply()
    }

    fun getLoginCredentials(): UserData? {
        return Gson().fromJson(
            sharedPreferences.getString(AppConstant.CONSTANT_CREDENTIALS, ""),
            UserData::class.java
        )
    }


}