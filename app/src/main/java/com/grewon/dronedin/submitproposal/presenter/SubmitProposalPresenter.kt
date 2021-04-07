package com.grewon.dronedin.submitproposal.presenter


import com.google.gson.Gson
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.CreateJobsBean
import com.grewon.dronedin.server.params.CreateJobsParams
import com.grewon.dronedin.server.params.SubmitProposalParams
import com.grewon.dronedin.submitproposal.contract.SubmitProposalContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File

class SubmitProposalPresenter : SubmitProposalContract.Presenter {


    private var view: SubmitProposalContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: SubmitProposalContract.View) {
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


    override fun submitProposal(params: SubmitProposalParams) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)



        builder.addFormDataPart("job_id", params.job_id!!)
        builder.addFormDataPart("proposal_title", params.proposal_title.toString())
        builder.addFormDataPart("proposal_description", params.proposal_description.toString())
        builder.addFormDataPart("proposal_milestone", params.proposal_milestone.toString())
        builder.addFormDataPart("proposal_total_price", params.proposal_total_price.toString())
        if (params.proposal_milestone == AppConstant.NEW_MILESTONE) {
            builder.addFormDataPart("milestone", Gson().toJson(params.milestone))
        }
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

        api.submitProposal(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onSubmitProposalSuccessFully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onSubmitProposalFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            SubmitProposalParams::class.java
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