package com.grewon.dronedin.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

abstract class NetworkCall<T> : SingleObserver<T> {

    abstract fun onSuccessResponse(t: T)

    abstract fun onFailedResponse(error: Any?)

    abstract fun onException(e: Throwable?)

    override fun onSubscribe(d: Disposable) {

    }

    override fun onSuccess(t: T) {

    }

    override fun onError(e: Throwable) {
        when (e) {
            is HttpException -> {
                if (e != null) {
                    val type = object : TypeToken<Any>() {}.type
                    val errorResponse: Any? =
                        Gson().fromJson(Any::class.java, type)

                }

            }
            else -> {
                onException(e)
            }
        }
    }
}