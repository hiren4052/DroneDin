package com.grewon.dronedin.clientjobs.presenter


import com.google.gson.Gson
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.clientjobs.contract.ClientProposalContract
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.GetJobsParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class ClientProposalPresenter : ClientProposalContract.Presenter {


    private var view: ClientProposalContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: ClientProposalContract.View) {
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


    override fun getClientProposals(jobId: String, page: Int) {

        val map = HashMap<String, Any>()

        map["job_id"] = jobId
        map["page"] = page

        api.getClientProposalList(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<ProposalsDataBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: ProposalsDataBean) {

                    view?.onProposalGetSuccessful(dataBean)


                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onProposalGetFailed(
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