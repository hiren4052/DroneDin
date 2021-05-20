package com.grewon.dronedin.invitepilot.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.invitepilot.contract.PilotInviteContract
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.FilterParams
import com.grewon.dronedin.server.params.LoginParams
import com.grewon.dronedin.server.params.PilotInviteParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class PilotInvitePresenter : PilotInviteContract.Presenter {


    private var view: PilotInviteContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: PilotInviteContract.View) {
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


    override fun getPilotData(filterParams: FilterParams) {

        val map = HashMap<String, Any>()

        if (filterParams.category != null) {
            map["category"] = filterParams.category.toString()
        }


        if (filterParams.skill != null) {
            map["skill"] = filterParams.skill.toString()
        }

        if (filterParams.equipment != null) {
            map["equipment"] = filterParams.equipment.toString()
        }

        if (filterParams.price != null) {
            map["price"] = filterParams.price.toString()
        }

        if (filterParams.page != null) {
            map["page"] = filterParams.page.toString()
        }


        api.getPilotList(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<PilotDataBean>(filterParams.toString()) {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: PilotDataBean) {
                    view?.onPilotDataGetSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onPilotDataGetFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            CommonMessageBean::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })
    }

    override fun invitePilots(invitePilots: PilotInviteParams) {
        view?.showProgress()
        api.invitePilots(invitePilots)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>(invitePilots.toString()) {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onPilotInviteSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onPilotInviteFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            PilotInviteParams::class.java
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