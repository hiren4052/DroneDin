package com.grewon.dronedin.dagger.module

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.locationapp.server.CommonResponse
import dagger.Module
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.google.gson.Gson
import dagger.Provides
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.utils.AnalyticsUtils
import com.grewon.dronedin.utils.PreferenceUtils

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule(val context: DroneDinApp, var baseURL: String) {


    @Singleton
    @Provides
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    fun provideOkhttpClient(preferenceUtils: PreferenceUtils): OkHttpClient {

        val httpClient = OkHttpClient.Builder()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")


            if (!preferenceUtils.getAuthToken()?.isEmpty()!!) {
               // val authToken: String = preferenceUtils.getAuthToken()!!
                val authToken: String = "6049963ad5411Q0JIUWtuU2xPeUtqcldEOE5KZjU5TWJ0MzdwNHdGZ3EwRWN4R2loZA=="
                requestBuilder.addHeader("X-Auth-Token", authToken.trim())
                LogX.E("X-Auth-Token -> " + preferenceUtils.getAuthToken())

            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        httpClient.interceptors().add(Interceptor { chain ->
            val response: Response = chain.proceed(chain.request())
            val body: ResponseBody = response.body()!!
            val bodyString = body.string()
            val apiUrl = response.request().url()
            val bodyRequstString = response.request().body()
            val formBodyBuilder = bodyRequstString

            try {
                Gson().fromJson(bodyString, CommonResponse::class.java)
            } catch (e: java.lang.Exception) {
                AnalyticsUtils.setCustomCrashlytics(
                    Gson().toJson(formBodyBuilder),
                    apiUrl.toString(),
                    bodyString
                )
            }

            val contentType: MediaType? = body.contentType()



            response.newBuilder().body(ResponseBody.create(contentType, bodyString)).build()

        })

        return httpClient.addInterceptor(interceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    fun provideApiInterface(gson: Gson, okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

}