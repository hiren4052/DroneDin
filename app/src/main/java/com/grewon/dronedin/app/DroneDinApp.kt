package com.grewon.dronedin.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.StrictMode
import android.widget.ImageView
import android.widget.Toast
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.grewon.dronedin.dagger.component.AppComponent
import com.grewon.dronedin.R
import com.grewon.dronedin.dagger.component.DaggerAppComponent
import com.grewon.dronedin.dagger.module.*
import com.grewon.dronedin.server.UserData
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import kotlinx.android.synthetic.main.activity_sign_up_type.*
import net.danlew.android.joda.JodaTimeAndroid

class DroneDinApp : MultiDexApplication() {

    private lateinit var component: AppComponent

    companion object {

        lateinit var instance: DroneDinApp

        var loadingDialogMessage: String = ""

        fun getAppInstance(): DroneDinApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        MultiDex.install(this)
        JodaTimeAndroid.init(this)
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
        setDialogMessage(getString(R.string.loading))
    }

    fun setDialogMessage(message: String) {
        loadingDialogMessage = message
    }

    private fun initDagger() {

        component = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule(this, AppConstant.API_URL))
            .signInModule(SignInModule())
            .profileModule(ProfileModule())
            .commonDataModule(CommonDataModule())
            .inviteModule(InviteModule())
            .clientJobsModule(ClientJobsModule())
            .filterModule(FilterModule())
            .pilotJobsModule(PilotJobsModule())
            .milestoneModule(MilestoneModule())
            .messageModule(MessageModule())
            .build()
    }


    fun getAppComponent(): AppComponent {
        return component
    }

    fun loadGifImage(drawableImage: Int, imageView: ImageView) {
        Glide.with(this).asGif().load(drawableImage)
            .addListener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false;
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.setLoopCount(1);
                    return false;
                }

            }).into(imageView)

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
            AppConstant.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        return if (preference.getString(AppConstant.AUTH_TOKEN, "") != null) preference.getString(
            AppConstant.AUTH_TOKEN,
            ""
        )!! else ""
    }

    fun getUserId(): String {
        val preference = getSharedPreferences(
            AppConstant.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        val userData = Gson().fromJson(
            preference.getString(AppConstant.CONSTANT_CREDENTIALS, ""),
            UserData::class.java
        )
        return userData.data?.userId.toString()
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