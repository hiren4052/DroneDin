package com.grewon.dronedin.review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.review.adapter.ReviewsAdapter
import com.grewon.dronedin.review.adapter.WhiteScreenReviewsAdapter
import com.grewon.dronedin.server.ReviewsDataBean
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class ReviewActivity : BaseActivity(), View.OnClickListener {

    private var reviewsAdapter: WhiteScreenReviewsAdapter? = null
    private var reviewList: ArrayList<ReviewsDataBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        setClicks()
        initView()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        txt_toolbar_title.text = getString(R.string.reviews)


        reviewList = intent.getParcelableArrayListExtra(AppConstant.BEAN)


        if (reviewList != null && reviewList?.size!! > 0) {
            review_recycle.layoutManager = LinearLayoutManager(this)
            reviewsAdapter = WhiteScreenReviewsAdapter(this)
            review_recycle.adapter = reviewsAdapter
            reviewsAdapter?.addItemsList(reviewList!!)
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }
}