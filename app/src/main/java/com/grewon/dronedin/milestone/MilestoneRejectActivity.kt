package com.grewon.dronedin.milestone

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import kotlinx.android.synthetic.main.activity_milestone_completion_request.*
import kotlinx.android.synthetic.main.activity_milestone_reject.*
import kotlinx.android.synthetic.main.activity_milestone_reject.im_back
import kotlinx.android.synthetic.main.activity_milestone_reject.top_image

class MilestoneRejectActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_reject)
        initView()
        setClicks()
    }


    private fun initView() {
        Glide.with(this).asGif().load(R.drawable.completed_job).into(top_image)

    }


    private fun setClicks() {
        im_back.setOnClickListener(this)
        txt_send.setOnClickListener(this)
        txt_cancel.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.txt_send -> {
                finish()
            }
            R.id.txt_cancel -> {
                finish()
            }

        }
    }
}