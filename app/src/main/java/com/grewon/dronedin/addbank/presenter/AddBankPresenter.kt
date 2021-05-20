package com.grewon.dronedin.addbank.presenter

import com.google.gson.Gson
import com.grewon.dronedin.addbank.contract.AddBankContract
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.AddBankParams
import com.grewon.dronedin.server.params.UpdateBankParams
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


class AddBankPresenter : AddBankContract.Presenter {

    private var view: AddBankContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: AddBankContract.View) {
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

    override fun createBank(addCardParams: AddBankParams) {
        view?.showProgress()

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)


        builder.addFormDataPart("acc_holder_firstname", addCardParams.accHolderFirstname!!)
        builder.addFormDataPart("acc_holder_lastname", addCardParams.accHolderLastname.toString())
        builder.addFormDataPart("bsb_number", addCardParams.bsbNumber.toString())
        builder.addFormDataPart("acc_number", addCardParams.accNumber.toString())

        if (!ValidationUtils.isEmptyFiled(addCardParams.birthDate.toString())) {
            builder.addFormDataPart("birth_date", addCardParams.birthDate.toString())
        }

        if (addCardParams.document != null && !ValidationUtils.isEmptyFiled(addCardParams.document.toString())) {
            if (!addCardParams.document.toString().contains("http")) {
                val file = File(addCardParams.document.toString())
                builder.addFormDataPart(
                    "document",
                    file.name,
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                )
            }
        }


        val requestBody = builder.build()


        api.createBankAccount(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<BankDataBean>(addCardParams.toString()) {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: BankDataBean) {
                    view?.hideProgress()
                    view?.onCreateBankSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onCreateBankFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            AddBankParams::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })

    }

    override fun updateBank(addCardParams: UpdateBankParams) {
        view?.showProgress()

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)


        builder.addFormDataPart("acc_holder_firstname", addCardParams.accHolderFirstname!!)
        builder.addFormDataPart("acc_holder_lastname", addCardParams.accHolderLastname.toString())
        builder.addFormDataPart("birth_date", addCardParams.birthDate.toString())

        if (addCardParams.document != null) {
            if (!addCardParams.document.toString().contains("http")) {
                val file = File(addCardParams.document)
                builder.addFormDataPart(
                    "document",
                    file.name,
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                )
            }
        }


        val requestBody = builder.build()


        api.updateBankAccount(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<BankDataBean>(addCardParams.toString()) {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: BankDataBean) {
                    view?.hideProgress()
                    view?.onCreateBankSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onCreateBankFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            AddBankParams::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })

    }

    override fun retrieveBankAccount() {

        view?.showProgress()

        api.getRetrieveBank()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<RetriveAccount>("") {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: RetriveAccount) {
                    view?.hideProgress()
                    view?.onRetrieveBankSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onRetrieveBankFailed(
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