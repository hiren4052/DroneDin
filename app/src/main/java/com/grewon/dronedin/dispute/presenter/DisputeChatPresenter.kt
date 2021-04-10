package com.grewon.dronedin.dispute.presenter

import com.google.gson.Gson
import com.grewon.dronedin.dispute.contract.DisputeChatContract
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.SentDisputeMessageParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File

class DisputeChatPresenter : DisputeChatContract.Presenter {


    private var view: DisputeChatContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: DisputeChatContract.View) {
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


    override fun sentDisputeMessage(sentMessageParams: SentDisputeMessageParams) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)


        builder.addFormDataPart("dispute_id", sentMessageParams.dispute_id!!)
        builder.addFormDataPart("msg_type", sentMessageParams.msg_type.toString())

        if (sentMessageParams.msg_type == "File") {
            val file = File(sentMessageParams.msg!!)
            builder.addFormDataPart(
                "msg",
                file.name,
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            )
        } else {
            builder.addFormDataPart("msg", sentMessageParams.msg!!.toString())
        }


        val requestBody = builder.build()

        view?.showProgress()

        api.sendDisputeMessage(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<SentDisputeChatBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: SentDisputeChatBean) {
                    view?.hideProgress()
                    view?.onMessageSentSuccessfully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onMessageSendFailed(
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

    override fun getDisputeOldMessage(offsetId: String, disputeId: String) {
        view?.showTopProgress()
        val params = HashMap<String, Any>()
        params["offsetID"] = offsetId
        params["dispute_id"] = disputeId

        api.getDisputeOldMessage(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<DisputeChatDataBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: DisputeChatDataBean) {
                    view?.hideTopProgress()
                    view?.onOldMessageGetSuccessfully(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideTopProgress()
                    LogX.E(errorBean.toString())
                    view?.onOldMessageGetFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            CommonMessageBean::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideTopProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })
    }

    override fun getDisputeNewMessage(offsetId: String, disputeId: String) {
        val params = HashMap<String, Any>()
        params["offsetID"] = offsetId
        params["dispute_id"] = disputeId

        api.getDisputeNewMessage(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<DisputeChatDataBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: DisputeChatDataBean) {
                    view?.onNewMessageGetSuccessfully(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onNewMessageGetFailed(
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