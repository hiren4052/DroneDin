package com.grewon.dronedin.review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import kotlinx.android.synthetic.main.activity_submit_review.*

class SubmitReviewActivity : BaseActivity(), View.OnClickListener {
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
        Glide.with(this).asGif().load(R.drawable.review_animation).into(top_image)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.img_back->{
                finish()
            }
            R.id.txt_submit->{
                finish()
            }
        }
    }
}