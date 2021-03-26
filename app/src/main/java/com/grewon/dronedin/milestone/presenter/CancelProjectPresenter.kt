package com.grewon.dronedin.milestone.presenter


import com.google.gson.Gson
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.milestone.contract.CancelProjectContract
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.*
import com.grewon.dronedin.utils.ValidationUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File

class CancelProjectPresenter : CancelProjectContract.Presenter {


    private var view: CancelProjectContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: CancelProjectContract.View) {
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


    override fun cancelProject(jobId: String, requestType: String) {


        val params = HashMap<String, Any>()
        params["job_id"] = jobId
        if (!ValidationUtils.isEmptyFiled(requestType))
            params["cancel_project_option"] = requestType

        view?.showProgress()

        api.cancelProject(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onCancelSuccessFully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onCancelFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            CancelMilestoneParams::class.java
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