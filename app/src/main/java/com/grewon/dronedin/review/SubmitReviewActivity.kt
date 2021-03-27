package com.grewon.dronedin.review

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.review.contract.SubmitReviewContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.SubmitReviewParams
import com.grewon.dronedin.utils.ValidationUtils

import kotlinx.android.synthetic.main.activity_submit_review.*
import retrofit2.Retrofit
import javax.inject.Inject


class SubmitReviewActivity : BaseActivity(), View.OnClickListener, SubmitReviewContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var submitReviewPresenter: SubmitReviewContract.Presenter

    private var jobId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_review)
        setClicks()
        initView()
    }

    private fun setClicks() {
        im_back.setOnClickListener(this)
        txt_submit.setOnClickListener(this)
    }

    private fun initView() {

        jobId = intent.getStringExtra(AppConstant.ID).toString()

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        submitReviewPresenter.attachView(this)
        submitReviewPresenter.attachApiInterface(retrofit)

        DroneDinApp.getAppInstance().loadGifImage(R.drawable.review_animation, top_image)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.txt_submit -> {
                when {
                    rating_bar.rating == 0f -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_provide_rating))
                    }
                    ValidationUtils.isEmptyFiled(edt_bio.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_write_something_about_your_experince))
                    }
                    else -> {
                        val submitParams = SubmitReviewParams(
                            jobId,
                            rating_bar.rating.toString(),
                            edt_bio.text.toString()
                        )
                        submitReviewPresenter.submitReview(submitParams)
                    }
                }

            }
        }
    }

    override fun onReviewSubmitSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onReviewSubmitFailed(loginParams: SubmitReviewParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }
}