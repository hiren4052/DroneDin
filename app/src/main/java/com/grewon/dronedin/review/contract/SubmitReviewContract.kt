package com.grewon.dronedin.review.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface SubmitReviewContract {

    interface View : BaseContract.View {


        fun onReviewSubmitSuccessFully(loginParams: CommonMessageBean)

        fun onReviewSubmitFailed(loginParams: SubmitReviewParams)

        fun onApiException(error: Int)


    }

    interface Presenter : BaseContract.Presenter<View> {

        fun submitReview(params: SubmitReviewParams)

    }

}