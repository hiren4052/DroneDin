package com.grewon.dronedin.message.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.message.contract.ChatContract
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.SentMessageParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File

class ChatPresenter : ChatContract.Presenter {


    private var view: ChatContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: ChatContract.View) {
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


    override fun createChatRoom(createChatRoomParams: CreateChatRoomParams) {

        api.createChatRoom(createChatRoomParams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<ChatRoomBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: ChatRoomBean) {
                    view?.onCreateChatRoomSuccessful(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onCreateChatRoomFailed(
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

    override fun sentMessage(sentMessageParams: SentMessageParams) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)


        builder.addFormDataPart("chat_room_id", sentMessageParams.chat_room_id!!)
        builder.addFormDataPart("msg_type", sentMessageParams.msg_type.toString())
        builder.addFormDataPart("reciever_id", sentMessageParams.reciever_id.toString())

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

        api.sendMessage(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<SentChatBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: SentChatBean) {
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

    override fun getOldMessage(offsetId: String, chatRoomId: String) {
        view?.showTopProgress()
        val params = HashMap<String, Any>()
        params["offsetID"] = offsetId
        params["chat_room_id"] = chatRoomId

        api.getOldMessage(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<ChatDataBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: ChatDataBean) {
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

    override fun getNewMessage(offsetId: String, chatRoomId: String) {
        val params = HashMap<String, Any>()
        params["offsetID"] = offsetId
        params["chat_room_id"] = chatRoomId

        api.getNewMessage(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<ChatDataBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: ChatDataBean) {
                    view?.onOldMessageGetSuccessfully(dataBean)

                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onOldMessageGetFailed(
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