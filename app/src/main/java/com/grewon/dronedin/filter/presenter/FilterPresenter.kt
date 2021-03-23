package com.grewon.dronedin.filter.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.filter.contract.FilterContract
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.FilterParams
import com.grewon.dronedin.server.params.PilotInviteParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class FilterPresenter : FilterContract.Presenter {


    private var view: FilterContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: FilterContract.View) {
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
            .subscribe(object : NetworkCall<PilotDataBean>() {

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

    override fun savePilots(pilotId: String) {

        view?.showProgress()

        api.savePilots(pilotId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onPilotSaveSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onPilotSaveFailed(
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



    override fun getPilotJobs(filterParams: FilterParams) {
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


        api.getPilotFindJobs(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<PilotJobsDataBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: PilotJobsDataBean) {
                    view?.onJobsDataGetSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onJobsDataGetFailed(
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



    override fun saveJobs(jobId: String) {
        view?.showProgress()

        api.saveJobs(jobId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onJobsSaveSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onJobsSaveFailed(
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