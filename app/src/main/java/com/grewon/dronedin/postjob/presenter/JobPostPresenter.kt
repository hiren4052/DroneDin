package com.grewon.dronedin.postjob.presenter


import com.google.gson.Gson
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.network.NetworkCall
import com.grewon.dronedin.postjob.contract.JobPostContract
import com.grewon.dronedin.server.AppApi
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.CreateJobsBean
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.server.params.CreateJobsParams
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

class JobPostPresenter : JobPostContract.Presenter {


    private var view: JobPostContract.View? = null
    private lateinit var api: AppApi
    private lateinit var retrofit: Retrofit
    private val subscriptions = CompositeDisposable()


    override fun attachView(view: JobPostContract.View) {
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


    override fun postJob(params: CreateJobsParams) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        val skillSelectedId = params.skillList?.filter { it.isSelected == 1 }?.map { it.skillId?.toInt() }
        val equipmentSelectedId = params.equipmentsList?.filter { it.isSelected == 1 }?.map { it.equipmentId?.toInt() }

        builder.addFormDataPart("category_id", params.selectedCategoryId!!)
        builder.addFormDataPart("skill", Gson().toJson(skillSelectedId))
        builder.addFormDataPart("equipment", Gson().toJson(equipmentSelectedId))
        builder.addFormDataPart("job_title", params.jobTitle.toString())
        builder.addFormDataPart("job_description", params.jobDescription.toString())
        builder.addFormDataPart("job_address", params.jobAddress.toString())
        builder.addFormDataPart("job_latitude", params.jobLatitude.toString())
        builder.addFormDataPart("job_longitude", params.jobLongitude.toString())
        builder.addFormDataPart("total_price", params.jobTotalPrice.toString())
        builder.addFormDataPart("milestone", Gson().toJson(params.mileStones))

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

        api.postJob(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CreateJobsBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CreateJobsBean) {
                    view?.hideProgress()
                    view?.onPostJobSuccessFully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onPostJobFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            CreateJobsParams::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })


    }

    override fun editJob(jobId: String, params: CreateJobsParams) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        val skillSelectedId =
            params.skillList?.filter { it.isSelected == 1 }?.map { it.skillId?.toInt() }
        val equipmentSelectedId =
            params.equipmentsList?.filter { it.isSelected == 1 }?.map { it.equipmentId?.toInt() }

        builder.addFormDataPart("category_id", params.selectedCategoryId!!)
        builder.addFormDataPart("skill", Gson().toJson(skillSelectedId))
        builder.addFormDataPart("equipment", Gson().toJson(equipmentSelectedId))
        builder.addFormDataPart("job_title", params.jobTitle.toString())
        builder.addFormDataPart("job_description", params.jobDescription.toString())
        builder.addFormDataPart("job_address", params.jobAddress.toString())
        builder.addFormDataPart("job_latitude", params.jobLatitude.toString())
        builder.addFormDataPart("job_longitude", params.jobLongitude.toString())
        builder.addFormDataPart("total_price", params.jobTotalPrice.toString())

        val milestoneList = ArrayList<CreateMilestoneBean>()
        if (params.mileStones != null && params.mileStones!!.size > 0) {
            for (item in params.mileStones!!) {
                if (ValidationUtils.isEmptyFiled(item.milestoneId)) {
                    milestoneList.add(item)
                }
            }
            builder.addFormDataPart("milestone", Gson().toJson(milestoneList))

        }

        if (params.attachments != null) {
            for ((index, item) in params.attachments!!.withIndex()) {
                if (ValidationUtils.isEmptyFiled(item.attachmentId)) {
                    val file = File(item.filePath!!)
                    builder.addFormDataPart(
                        "attachment[]",
                        file.name,
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    )
                }
            }
        }


        val requestBody = builder.build()

        view?.showProgress()

        api.editJob(requestBody, jobId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CreateJobsBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CreateJobsBean) {
                    view?.hideProgress()
                    view?.onPostJobSuccessFully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onPostJobFailed(
                        Gson().fromJson(
                            errorBean.toString(),
                            CreateJobsParams::class.java
                        )
                    )
                }

                override fun onException(throwable: Throwable?) {
                    view?.hideProgress()
                    view?.onApiException(ErrorHandler.handleError(throwable!!))
                }


            })
    }

    override fun deleteAttachment(attachmentId: String) {
        api.deleteAttachment(attachmentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onDeleteAttachmentSuccessfully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onDeleteAttachmentFailed(
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

    override fun deleteMilestone(milestoneId: String) {
        api.deleteMilestone(milestoneId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NetworkCall<CommonMessageBean>() {

                override fun onSubscribeCall(disposable: Disposable) {
                    subscriptions.add(disposable)
                }

                override fun onSuccessResponse(dataBean: CommonMessageBean) {
                    view?.hideProgress()
                    view?.onDeleteMilestoneSuccessfully(dataBean)
                }

                override fun onFailedResponse(errorBean: Any?) {
                    view?.hideProgress()
                    LogX.E(errorBean.toString())
                    view?.onDeleteMilestoneFailed(
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