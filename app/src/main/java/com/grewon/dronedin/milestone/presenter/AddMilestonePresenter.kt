package com.grewon.dronedin.milestone.presenter


import com.google.gson.Gson
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.milestone.contract.AddMilestoneContract
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.AddMileStoneParams
import com.grewon.dronedin.server.params.SubmitMilestoneParams
import com.grewon.dronedin.server.params.SubmitOfferParams
import com.grewon.dronedin.server.params.SubmitProposalParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File

class AddMilestonePresenter : AddMilestoneContract.Presenter {


    private var view: AddMilestoneContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: AddMilestoneContract.View) {
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


    override fun addMilestone(params: AddMileStoneParams) {

        val map = HashMap<String, Any>()

        map["job_id"] = params.job_id!!
        map["milestone"] = Gson().toJson(params.mileStones)


        view?.showProgress()

        api.addMileStone(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onSubmitSuccessFully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onSubmitFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            AddMileStoneParams::class.java
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