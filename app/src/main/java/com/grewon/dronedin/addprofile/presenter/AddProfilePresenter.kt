package com.grewon.dronedin.addprofile.presenter


import com.google.gson.Gson
import com.grewon.dronedin.addprofile.contract.AddProfileContract
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.IdentificationBean
import com.grewon.dronedin.server.ProfileBean
import com.grewon.dronedin.server.params.ProfileUpdateParams
import com.grewon.dronedin.utils.TextUtils
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

class AddProfilePresenter : AddProfileContract.Presenter {


    private var view: AddProfileContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: AddProfileContract.View) {
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


    override fun getProfile() {
        view?.showProgress()
        api.myProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<ProfileBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: ProfileBean) {
                    view?.hideProgress()
                    view?.onProfileGetSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onProfileGetFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            ProfileUpdateParams::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })
    }

    override fun updateProfile(params: ProfileUpdateParams) {
        var frontSideImage: MultipartBody.Part? = null
        var backSideImage: MultipartBody.Part? = null
        var profileImage: MultipartBody.Part? = null

        if (params.profileImage?.contains("http") == true) {
            profileImage =
                MultipartBody.Part.createFormData(
                    "profile_image",
                    "",
                    RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        ""
                    )
                )
        } else {
            profileImage =
                MultipartBody.Part.createFormData(
                    "profile_image",
                    File(params.profileImage.toString()).name,
                    RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        File(params.profileImage.toString())
                    )
                )
        }


        if (!ValidationUtils.isEmptyFiled(params.proofId.toString())) {

            if (!ValidationUtils.isEmptyFiled(params.proofFrontSide.toString())) {
                if (params.proofFrontSide?.contains("http") == true) {
                    frontSideImage =
                        MultipartBody.Part.createFormData(
                            "proof_front_side",
                            "",
                            RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                ""
                            )
                        )
                } else {
                    frontSideImage =
                        MultipartBody.Part.createFormData(
                            "proof_front_side",
                            File(params.proofFrontSide.toString()).name,
                            RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                File(params.proofFrontSide.toString())
                            )
                        )
                }
            }

            if (!ValidationUtils.isEmptyFiled(params.proofBackSide.toString())) {

                if (params.proofBackSide?.contains("http") == true) {

                    backSideImage =
                        MultipartBody.Part.createFormData(
                            "proof_back_side",
                            "",
                            RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                ""
                            )
                        )
                } else {
                    backSideImage =
                        MultipartBody.Part.createFormData(
                            "proof_back_side",
                            File(params.proofBackSide.toString()).name,
                            RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                File(params.proofBackSide.toString())
                            )
                        )
                }
            }

        }

        val map = HashMap<String, RequestBody?>()
        map["user_name"] = TextUtils.createPartFromString(params.userName!!)
        map["user_phone_number"] = TextUtils.createPartFromString(params.userPhoneNumber!!)
        map["user_address"] = TextUtils.createPartFromString(params.userAddress!!)
        map["user_latitude"] = TextUtils.createPartFromString(params.userLatitude.toString())
        map["user_longitude"] = TextUtils.createPartFromString(params.userLongitude.toString())
        map["proof_id"] = TextUtils.createPartFromString(params.proofId.toString())
        map["user_front_text"] = TextUtils.createPartFromString(params.userFrontText.toString())
        map["user_back_text"] = TextUtils.createPartFromString(params.userBackText.toString())

        view?.showProgress()

        api.updateProfile(profileImage, frontSideImage, backSideImage, map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<ProfileBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: ProfileBean) {
                    view?.hideProgress()
                    view?.onProfileUpdateSuccessFully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onProfileUpdateFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            ProfileUpdateParams::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })
    }

    override fun getIdentificationsData() {
        api.getIdentificationsData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<IdentificationBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: IdentificationBean) {
                    view?.onIdentificationsDataGetSuccessful(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    LogX.E(errorBean.toString())
                    view?.onIdentificationsDataFailed(
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