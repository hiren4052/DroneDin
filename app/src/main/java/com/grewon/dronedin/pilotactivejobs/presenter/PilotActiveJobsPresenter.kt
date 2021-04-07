package com.grewon.dronedin.pilotactivejobs.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.pilotactivejobs.contract.PilotActiveJobsContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.FilterParams
import com.grewon.dronedin.server.params.GetJobsParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class PilotActiveJobsPresenter : PilotActiveJobsContract.Presenter {


    private var view: PilotActiveJobsContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()



    override fun attachView(view: PilotActiveJobsContract.View) {
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





    override fun getPilotActiveJobs(getJobsParams: GetJobsParams) {
        val map = HashMap<String, Any>()

        if (getJobsParams.page != null) {
            map["page"] = getJobsParams.page.toString()
        }

        if (getJobsParams.jobType != null) {
            map["job_type"] = getJobsParams.jobType.toString()
        }


        api.getPilotActiveJobs(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<PilotActiveJobsData>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: PilotActiveJobsData) {
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





}