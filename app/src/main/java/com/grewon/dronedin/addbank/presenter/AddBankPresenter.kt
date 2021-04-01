package com.grewon.dronedin.addbank.presenter


import com.google.gson.Gson
import com.grewon.dronedin.addbank.contract.AddBankContract
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.AddBankParams
import com.grewon.dronedin.server.params.AddCardParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

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

        api.createBankAccount(addCardParams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<BankDataBean>() {

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


}