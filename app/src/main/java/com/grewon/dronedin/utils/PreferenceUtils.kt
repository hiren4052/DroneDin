package com.grewon.dronedin.utils

import android.content.SharedPreferences
import com.grewon.dronedin.server.UserData
import com.google.gson.Gson
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.server.MainScreenData


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

    fun saveProfileData(response: MainScreenData) {
        val json = Gson().toJson(response)
        sharedPreferences.edit().putString(AppConstant.PROFILE_DATA, json).apply()
    }

    fun getProfileData(): MainScreenData? {
        return Gson().fromJson(
            sharedPreferences.getString(AppConstant.PROFILE_DATA, ""),
            MainScreenData::class.java
        )
    }

    fun getLoginCredentials(): UserData? {
        return Gson().fromJson(
            sharedPreferences.getString(AppConstant.CONSTANT_CREDENTIALS, ""),
            UserData::class.java
        )
    }


}