package com.grewon.dronedin.clientprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.review.adapter.WhiteScreenReviewsAdapter
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_client_profile.*
import kotlinx.android.synthetic.main.activity_client_profile.blur_img_user
import kotlinx.android.synthetic.main.activity_client_profile.im_back
import kotlinx.android.synthetic.main.activity_client_profile.im_message
import kotlinx.android.synthetic.main.activity_client_profile.review_recycle
import kotlinx.android.synthetic.main.activity_pilot_profile.*

class ClientProfileActivity : BaseActivity(), View.OnClickListener {


    private var reviewsAdapter: WhiteScreenReviewsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_profile)
        setClicks()
        initView()
    }

    private fun setClicks() {
        im_back.setOnClickListener(this)
        im_message.setOnClickListener(this)
    }

    private fun initView() {

        Glide.with(this).load(R.drawable.img_dummy)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(22, 3)))
            .into(blur_img_user)



        setReviewAdapter()

    }

    private fun setReviewAdapter() {
        review_recycle.layoutManager = LinearLayoutManager(this)
        reviewsAdapter = WhiteScreenReviewsAdapter(this)
        review_recycle.adapter = reviewsAdapter
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.im_message -> {
                startActivity(Intent(this, ChatActivity::class.java))
            }
        }
    }


}