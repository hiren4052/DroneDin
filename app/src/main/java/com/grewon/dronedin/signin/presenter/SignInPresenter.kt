package com.grewon.dronedin.signin.presenter


import com.evereats.app.server.*
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.signin.contract.SignInContract
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.params.LoginParams
import com.grewon.dronedin.server.params.SocialLoginParams
import com.grewon.dronedin.utils.AnalyticsUtils
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class SignInPresenter : SignInContract.Presenter {


    private var view: SignInContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()

    override fun attachView(view: SignInContract.View) {
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


    override fun userSocialLogin(params: SocialLoginParams) {


        view?.showProgress()

        api.socialLogin(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<UserData> {
                override fun onSubscribe(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccess(response: UserData) {
                    view?.hideProgress()
                    view?.onUserLoggedInSuccessful(response)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    AnalyticsUtils.setCustomCrashlytics(
                        params.toString(),
                        AppConstant.API_URL + "user_c/social_login",
                        e.printStackTrace().toString()
                    )

                    view?.hideProgress()
                    view?.onUserLoggedInFailed(ErrorHandler.handleError(e))

                }
            })
    }

    override fun userLogin(params: LoginParams) {
        view?.showProgress()

        api.simpleLogin(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<UserData> {
                override fun onSubscribe(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccess(response: UserData) {
                    view?.hideProgress()
                    view?.onUserLoggedInSuccessful(response)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    AnalyticsUtils.setCustomCrashlytics(
                        params.toString(),
                        AppConstant.API_URL + "user_c/login",
                        e.printStackTrace().toString()
                    )

                    view?.hideProgress()
                    view?.onUserLoggedInFailed(ErrorHandler.handleError(e))

                }
            })
    }


}