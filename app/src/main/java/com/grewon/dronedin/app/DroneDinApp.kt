package com.grewon.dronedin.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.StrictMode
import android.widget.Toast
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.FacebookSdk
import com.grewon.dronedin.dagger.component.AppComponent
import com.grewon.dronedin.dagger.module.NetworkModule
import com.grewon.dronedin.R
import com.grewon.dronedin.dagger.component.DaggerAppComponent
import com.grewon.dronedin.dagger.module.AppModule
import com.grewon.dronedin.dagger.module.SignInModule
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class DroneDinApp : MultiDexApplication() {

    private lateinit var component: AppComponent

    companion object {

        lateinit var instance: DroneDinApp


        fun getAppInstance(): DroneDinApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        MultiDex.install(this)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath(AppConstant.FONT_FAMILY)
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        initDagger()
        createNotificationChannel()
    }

    private fun initDagger() {

        component = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule(this, AppConstant.API_URL))
            .signInModule(SignInModule())
            .build()
    }


    fun getAppComponent(): AppComponent {
        return component
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(AppConstant.CHANNEL_ID, AppConstant.CHANNEL_NAME, importance)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }

    fun showToast(
        message: String
    ) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    fun getAuthToken(): String {
        val preference = getSharedPreferences(
            "MainPreference",
            Context.MODE_PRIVATE
        )
        return if (preference.getString(AppConstant.AUTH_TOKEN, "") != null) preference.getString(
            AppConstant.AUTH_TOKEN,
            ""
        )!! else ""
    }

    fun getDeviceInformation(): String {
        return Build.MODEL + "|" + Build.MANUFACTURER + "|" + Build.BRAND + "|" + Build.VERSION.SDK + "|" + Build.BRAND + "|" + Build.VERSION.RELEASE
    }

    fun isOnline(): Boolean {
        var result = false
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            @Suppress("DEPRECATION")
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }


}