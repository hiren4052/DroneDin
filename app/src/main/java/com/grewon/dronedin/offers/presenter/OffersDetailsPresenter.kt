package com.grewon.dronedin.offers.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.offers.contract.OffersDetailsContract
import com.grewon.dronedin.server.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class OffersDetailsPresenter : OffersDetailsContract.Presenter {


    private var view: OffersDetailsContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: OffersDetailsContract.View) {
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


    override fun getOffersDetails(offersId: String, jobType: String) {
        view?.showOnScreenProgress()
        val map = HashMap<String, Any>()


        map["job_id"] = offersId
        map["job_type"] = jobType

        api.getOffersDetailsBean(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<OffersDetailBean>(map.toString()) {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: OffersDetailBean) {
                    view?.hideOnScreenProgress()
                    view?.onOffersDetailSuccessfully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideOnScreenProgress()
                    LogX.E(errorBean.toString())
                    view?.onOffersDetailFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            CommonMessageBean::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideOnScreenProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })
    }

    override fun acceptDeclineOffers(offersId: String, offerStatus: String) {

        view?.showProgress()

        val map=HashMap<String,Any>()
        map["offer_status"]=offerStatus
        map["offer_id"]=offersId

        api.offerAcceptDecline(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>(map.toString()) {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onOffersStatusChangedSuccessfully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onOffersStatusChangedFailed(
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