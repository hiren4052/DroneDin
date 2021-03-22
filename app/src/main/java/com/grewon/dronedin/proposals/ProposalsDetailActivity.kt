package com.grewon.dronedin.proposals

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.milestone.adapter.MileStoneAdapter
import kotlinx.android.synthetic.main.activity_proposals_detail.*

class ProposalsDetailActivity : BaseActivity(), View.OnClickListener {
    private var mileStoneAdapter: MileStoneAdapter? = null
    private var jobsImageAdapter: JobAttachmentsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposals_detail)
        initView()
        setClicks()
    }


    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        setMileStoneAdapter()
        setImageAdapter()


    }

    private fun setImageAdapter() {
        image_recycle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = JobAttachmentsAdapter(this)
        image_recycle.adapter = jobsImageAdapter

    }

    private fun setMileStoneAdapter() {
        mile_stone_recycle.layoutManager = LinearLayoutManager(this)
        mileStoneAdapter = MileStoneAdapter(this)
        mile_stone_recycle.adapter = mileStoneAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }
}