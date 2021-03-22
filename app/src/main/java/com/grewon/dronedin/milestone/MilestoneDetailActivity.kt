package com.grewon.dronedin.milestone

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import kotlinx.android.synthetic.main.activity_submit_milestone.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class MilestoneDetailActivity : BaseActivity(), View.OnClickListener {

    private var jobsImageAdapter: JobAttachmentsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_detail)
        initView()
        setClicks()
    }


    private fun initView() {
        txt_toolbar_title.text = getString(R.string.milestone_details)
        setImageAdapter()
    }

    private fun setImageAdapter() {
        image_recycle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = JobAttachmentsAdapter(this)
        image_recycle.adapter = jobsImageAdapter

    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }

        }
    }


}