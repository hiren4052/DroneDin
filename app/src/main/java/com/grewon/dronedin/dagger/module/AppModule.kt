package com.grewon.dronedin.dagger.module

import android.content.Context
import android.content.SharedPreferences
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.utils.PreferenceUtils


import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context: DroneDinApp) {

    @Singleton
    @Provides
    fun providesApplicationContext(): Context {
        return context
    }

    @Singleton
    @Provides
    public fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(AppConstant.PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    public fun providePreferenceUtils(sharedPreferences: SharedPreferences): PreferenceUtils {
        return PreferenceUtils(sharedPreferences)
    }


}