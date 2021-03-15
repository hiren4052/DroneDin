package com.grewon.dronedin.forgotpassword.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.forgotpassword.contract.ForgotPasswordContract
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.ForgotPasswordParams
import com.grewon.dronedin.server.params.RegisterParams
import com.grewon.dronedin.server.params.SocialRegisterParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class ForgotPasswordPresenter : ForgotPasswordContract.Presenter {


    private var view: ForgotPasswordContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: ForgotPasswordContract.View) {
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


    override fun forgotPassword(params: ForgotPasswordParams) {
        view?.showProgress()
        api.forgotPassword(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onEmailSentSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onEmailSentFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            ForgotPasswordParams::class.java
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