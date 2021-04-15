package com.grewon.dronedin.earnings.presenter


import com.google.gson.Gson
import com.grewon.dronedin.earnings.contract.EarningContract
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class EarningPresenter : EarningContract.Presenter {


    private var view: EarningContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: EarningContract.View) {
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


    override fun getMonthlyEarning() {
        view?.showProgress()
        api.getMonthlyEarning("monthly")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<MonthlyEarningBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: MonthlyEarningBean) {
                    view?.hideProgress()
                    view?.onMonthlyDataGetSuccessful(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.hideProgress()
                    view?.onMonthlyDataGetFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            CommonMessageBean::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })

    }

    override fun getWeeklyEarning() {
        view?.showProgress()
        api.getWeeklyEarning("weekly")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<WeeklyDataBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: WeeklyDataBean) {
                    view?.hideProgress()
                    view?.onWeeklyDataGetSuccessful(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.hideProgress()
                    view?.onWeeklyDataGetFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            CommonMessageBean::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })
    }

    override fun getEarningsData(offsetCount: Int) {
        view?.showProgress()
        api.getEarningsData(offsetCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<EarningsDataBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: EarningsDataBean) {
                    view?.hideProgress()
                    view?.onEarningsDataGetSuccessful(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.hideProgress()
                    view?.onEarningsDataGetFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            CommonMessageBean::class.java
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