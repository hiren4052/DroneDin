package com.grewon.dronedin.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.StrictMode
import android.widget.ImageView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.app.locationapp.server.CommonResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.dagger.component.AppComponent
import com.grewon.dronedin.dagger.component.DaggerAppComponent
import com.grewon.dronedin.dagger.module.*
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.onlineoffline.OnlineOfflineService
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.utils.AnalyticsUtils
import com.grewon.dronedin.utils.PreferenceUtils
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import net.danlew.android.joda.JodaTimeAndroid
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class DroneDinApp : MultiDexApplication(), AppLifecycleHandler.LifeCycleDelegate {

    private lateinit var component: AppComponent

    companion object {

        lateinit var instance: DroneDinApp

        var loadingDialogMessage: String = ""
        var isChatScreen = false

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

        val lifeCycleHandler = AppLifecycleHandler(this)
        registerLifecycleHandler(lifeCycleHandler)

        initDagger()

        createNotificationChannel()
        setDialogMessage(getString(R.string.loading))

    }

    private fun registerLifecycleHandler(AppLifecycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(AppLifecycleHandler)
        registerComponentCallbacks(AppLifecycleHandler)
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
            .mainModule(MainModule())
            .membershipModule(MembershipModule())
            .disputeModule(DisputeModule())
            .earningDataModule(EarningDataModule())
            .build()
    }


    fun getAppComponent(): AppComponent {
        return component
    }

    @Synchronized
    fun getApi(preferenceUtils: PreferenceUtils): AppApi {
        val httpClient = OkHttpClient.Builder()
        //log level
        val interceptor = HttpLoggingInterceptor()




        httpClient.addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")

            if (!preferenceUtils.getAuthToken()?.isEmpty()!!) {
                LogX.E("AuthToken -> " + preferenceUtils.getAuthToken())
                val authToken: String = preferenceUtils.getAuthToken()!!
                requestBuilder.addHeader("X-Authorization", authToken)

            }

            val request = requestBuilder.build()

            chain.proceed(request)

        }

        httpClient.interceptors().add(Interceptor { chain ->
//            val request = chain.request()
//
//            val response = chain.proceed(request)
//
//            response.code()

            val response: Response = chain.proceed(chain.request())
            val body: ResponseBody = response.body()!!
            val bodyString = body.string()
            val apiUrl = response.request().url()
            val bodyRequstString = response.request().body()
            val formBodyBuilder = bodyRequstString

            try {
                Gson().fromJson(bodyString, CommonResponse::class.java)
            } catch (e: Exception) {
            }

            val contentType: MediaType? = body.contentType()
            if (response.code() == 401) {
                val intent = Intent(AppConstant.SESSION_BROADCAST) //action: "msg"
                intent.putExtra(AppConstant.DATA_TYPE, "yes")
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
            response.newBuilder().body(ResponseBody.create(contentType, bodyString)).build()
        })


        val client = httpClient.addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(AppConstant.API_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
        return retrofit.create(AppApi::class.java)
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

    override fun onAppBackgrounded() {

        val intent = Intent(this, OnlineOfflineService::class.java)
        intent.putExtra(AppConstant.TAG, "no")
        startService(intent)

    }

    override fun onAppForegrounded() {

        val intent = Intent(this, OnlineOfflineService::class.java)
        intent.putExtra(AppConstant.TAG, "yes")
        startService(intent)

    }


}