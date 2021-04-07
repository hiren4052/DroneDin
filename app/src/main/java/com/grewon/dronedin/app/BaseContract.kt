package com.grewon.dronedin.app


import retrofit2.Retrofit

interface BaseContract {

    interface Presenter<in T> {
        fun attachView(view: T)
        fun attachApiInterface(retrofit: Retrofit)
        fun detachView()
    }

    interface View {
        fun showProgress()
        fun hideProgress()
    }

}