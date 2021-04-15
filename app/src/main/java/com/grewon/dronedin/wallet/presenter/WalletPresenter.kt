package com.grewon.dronedin.wallet.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.WithdrawParams
import com.grewon.dronedin.wallet.contract.WalletContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class WalletPresenter : WalletContract.Presenter {


    private var view: WalletContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: WalletContract.View) {
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


    override fun getEarningsData(offsetCount: Int) {
        api.getEarningsData(offsetCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<EarningsDataBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: EarningsDataBean) {
                    view?.onWalletDataGetSuccessful(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onWalletDataGetFailed(
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

    override fun withdrawAmount(withdrawParams: WithdrawParams) {
        view?.showProgress()
        api.withdrawAmount(withdrawParams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onWithdrawDataSuccessful(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.hideProgress()
                    view?.onWithdrawDataFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            WithdrawParams::class.java
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