package com.grewon.dronedin.signup.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.LoginParams
import com.grewon.dronedin.server.params.RegisterParams
import com.grewon.dronedin.server.params.SocialLoginParams
import com.grewon.dronedin.server.params.SocialRegisterParams
import com.grewon.dronedin.signup.contract.SignUpContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class SignUpPresenter : SignUpContract.Presenter {


    private var view: SignUpContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: SignUpContract.View) {
        this.view = view
    }

    override fun detachView() {
        subscriptions.clear()
        this.view = null
    }

    override fun attachApiInterface(retrofit: Retrofit) {
        this.retrofit = retrofit
        this.api = retrofit.create(AppApi::class.java)
    }

    override fun userSocialRegister(params: SocialRegisterParams) {
        view?.showProgress()
        api.socialRegister(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<UserData>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: UserData) {
                    view?.hideProgress()
                    view?.onUserRegisterSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onUserSocialRegisterFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            SocialRegisterParams::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })
    }

    override fun userRegister(params: RegisterParams) {
        view?.showProgress()
        api.simpleRegister(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<UserData>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: UserData) {
                    view?.hideProgress()
                    view?.onUserRegisterSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onUserRegisterFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            RegisterParams::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })
    }



}