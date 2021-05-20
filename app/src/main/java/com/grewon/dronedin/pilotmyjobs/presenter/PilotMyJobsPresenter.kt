package com.grewon.dronedin.pilotmyjobs.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.pilotmyjobs.contract.PilotMyJobsContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.GetJobsParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class PilotMyJobsPresenter : PilotMyJobsContract.Presenter {


    private var view: PilotMyJobsContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: PilotMyJobsContract.View) {
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


    override fun getPilotOffersMyJobs(filterParams: GetJobsParams) {
        val map = HashMap<String, Any>()

        if (filterParams.jobType != null) {
            map["job_type"] = filterParams.jobType.toString()
        }

        map["page"] = filterParams.page

        api.getPilotOffersJobs(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<OffersDataBean>(filterParams.toString()) {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: OffersDataBean) {
                    view?.onOffersDataGetSuccessful(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onDataGetFailed(
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

    override fun getPilotInvitationsMyJobs(filterParams: GetJobsParams) {
        val map = HashMap<String, Any>()

        if (filterParams.jobType != null) {
            map["job_type"] = filterParams.jobType.toString()
        }

        map["page"] = filterParams.page

        api.getPilotInvitationsJobs(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<InvitationsDataBean>(filterParams.toString()) {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: InvitationsDataBean) {
                    view?.onInvitationsDataGetSuccessful(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onDataGetFailed(
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

    override fun getPilotProposalsMyJobs(filterParams: GetJobsParams) {
        val map = HashMap<String, Any>()

        if (filterParams.jobType != null) {
            map["job_type"] = filterParams.jobType.toString()
        }

        map["page"] = filterParams.page

        api.getPilotProposalsJobs(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<ProposalsDataBean>(filterParams.toString()) {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: ProposalsDataBean) {
                    view?.onProposalDataGetSuccessful(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onDataGetFailed(
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


}