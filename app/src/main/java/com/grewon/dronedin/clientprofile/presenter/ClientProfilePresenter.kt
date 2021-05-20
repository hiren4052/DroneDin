package com.grewon.dronedin.clientprofile.presenter


import com.google.gson.Gson
import com.grewon.dronedin.clientprofile.contract.ClientProfileContract
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class ClientProfilePresenter : ClientProfileContract.Presenter {


    private var view: ClientProfileContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: ClientProfileContract.View) {
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


    override fun getClientProfile(profileId: String) {
        view?.showOnScreenProgress()


        api.getClientDetail(profileId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<ClientProfileBean>(profileId) {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: ClientProfileBean) {
                    view?.hideOnScreenProgress()
                    view?.onClientProfileDetailSuccessfully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideOnScreenProgress()
                    LogX.E(errorBean.toString())
                    view?.onClientProfileDetailFailed(
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

}