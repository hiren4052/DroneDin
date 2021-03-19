package com.grewon.dronedin.network

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

abstract class NetworkCall<T> : SingleObserver<T> {

    abstract fun onSuccessResponse(dataBean: T)

    abstract fun onSubscribeCall(disposable: Disposable)

    abstract fun onFailedResponse(errorBean: Any?)

    abstract fun onException(throwable: Throwable?)

    override fun onSubscribe(d: Disposable) {
        onSubscribeCall(d)
    }

    override fun onSuccess(t: T) {
        onSuccessResponse(t)
    }

    override fun onError(e: Throwable) {
        when (e) {
            is HttpException -> {
                if (e != null) {
                    if(e.response().code()==400) {
                        val body = e.response()?.errorBody()
                        val adapter = Gson().getAdapter(Any::class.java)
                        val errorParser = adapter.fromJson(body?.string())
                        val json = Gson().toJson(errorParser)
                        onFailedResponse(json)
                    }else{
                        onException(e)
                    }
                }
            }
            else -> {
                onException(e)
            }
        }
    }
}