package com.grewon.dronedin.portfolio.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.portfolio.contract.PortFolioContract
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.AddPortFolioParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File

class PortFolioPresenter : PortFolioContract.Presenter {


    private var view: PortFolioContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: PortFolioContract.View) {
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


    override fun addPortFolio(params: AddPortFolioParams) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)


        builder.addFormDataPart("portfolio_title", params.portfolio_title!!)
        builder.addFormDataPart("portfolio_desc", params.portfolio_desc.toString())

        if (params.attachments != null) {
            for (item in params.attachments!!) {
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

        api.createPortFolio(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onAddPortFolioSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onAddPortFolioFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            AddPortFolioParams::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })
    }

    override fun updatePortFolio(params: AddPortFolioParams, portFolioId: String) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("portfolio_title", params.portfolio_title!!)
        builder.addFormDataPart("portfolio_desc", params.portfolio_desc.toString())

        if (params.attachments != null) {
            for (item in params.attachments!!) {
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

        api.updatePortFolio(requestBody, portFolioId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onUpdatePortFolioSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onUpdatePortFolioFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            AddPortFolioParams::class.java
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