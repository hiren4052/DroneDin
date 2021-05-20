package com.grewon.dronedin.milestone.milestonesubmit.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.milestone.milestonesubmit.contract.SubmitMilestoneContract
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.CancelMilestoneStatusUpdateParams
import com.grewon.dronedin.server.params.SubmitMilestoneParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File

class SubmitMilestonePresenter : SubmitMilestoneContract.Presenter {


    private var view: SubmitMilestoneContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: SubmitMilestoneContract.View) {
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


    override fun submitMilestone(params: SubmitMilestoneParams) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)


        builder.addFormDataPart("milestone_id", params.milestone_id!!)
        builder.addFormDataPart("milestone_request_note", params.milestone_request_note!!)
        if (params.attachments != null) {
            for ((index, item) in params.attachments!!.withIndex()) {
                val file = File(item.filePath!!)
                builder.addFormDataPart(
                    "attachment[]",
                    file.name,
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                )
            }
        }

        val requestBody = builder.build()

        view?.showProgress()

        api.submitMilestone(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>(params.toString()) {

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
                            SubmitMilestoneParams::class.java
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