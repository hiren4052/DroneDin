package com.grewon.dronedin.offers.presenter


import com.google.gson.Gson
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.offers.contract.CreateOffersContract
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.CardDataBean
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.SubmitOfferParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File

class CreateOffersPresenter : CreateOffersContract.Presenter {


    private var view: CreateOffersContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: CreateOffersContract.View) {
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


    override fun submitOffer(params: SubmitOfferParams) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)



        builder.addFormDataPart("job_id", params.job_id!!)
        builder.addFormDataPart("pilot_id", params.pilot_id!!)
        builder.addFormDataPart("offer_title", params.offer_title.toString())
        builder.addFormDataPart("offer_description", params.offer_description.toString())
        builder.addFormDataPart("offer_milestone", params.offer_milestone.toString())
        builder.addFormDataPart("offer_total_price", params.offer_total_price.toString())
        builder.addFormDataPart("user_wallet", params.user_wallet.toString())
        if (params.offer_milestone == AppConstant.NEW_MILESTONE) {
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

        api.submitOffer(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onSubmitOffersSuccessFully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onSubmitOffersFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            SubmitOfferParams::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })
    }


    override fun getCardData() {

        view?.showProgress()

        api.getCardData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CardDataBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CardDataBean) {
                    view?.hideProgress()
                    view?.onCardDataGetSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onCardDataGetFailed(
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