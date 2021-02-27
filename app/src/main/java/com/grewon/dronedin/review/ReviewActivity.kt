package com.grewon.dronedin.review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.review.adapter.ReviewsAdapter
import com.grewon.dronedin.review.adapter.WhiteScreenReviewsAdapter
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class ReviewActivity : BaseActivity(), View.OnClickListener {

    private var reviewsAdapter: WhiteScreenReviewsAdapter? = null
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
        review_recycle.layoutManager = LinearLayoutManager(this)
        reviewsAdapter = WhiteScreenReviewsAdapter(this)
        review_recycle.adapter = reviewsAdapter
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }
}