package com.grewon.dronedin.clientjobs.presenter


import com.google.gson.Gson
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.clientjobs.contract.ClientJobsContract
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.FilterParams
import com.grewon.dronedin.server.params.GetJobsParams
import com.grewon.dronedin.server.params.PilotInviteParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class ClientJobsPresenter : ClientJobsContract.Presenter {


    private var view: ClientJobsContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: ClientJobsContract.View) {
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


    override fun getClientJobs(filterParams: GetJobsParams) {
        val map = HashMap<String, Any>()

        if (filterParams.jobType != null) {
            map["job_type"] = filterParams.jobType.toString()
        }

        map["page"] = filterParams.page

        api.getClientJobs(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<JobsDataBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: JobsDataBean) {
                    when (filterParams.jobType) {
                        AppConstant.POSTED_JOB_STATUS -> {
                            view?.onPostedJobsGetSuccessful(dataBean)
                        }
                        AppConstant.ACTIVE_JOB_STATUS -> {
                            view?.onActiveJobsGetSuccessful(dataBean)
                        }
                        else -> {
                            view?.onJobsHistoryGetSuccessful(dataBean)
                        }
                    }
                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onJobsGetFailed(
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