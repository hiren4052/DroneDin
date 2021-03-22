package com.grewon.dronedin.clientjobs.posted

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.attachments.JobAttachmentsAdapter
import com.grewon.dronedin.milestone.adapter.MileStoneAdapter
import com.grewon.dronedin.offers.CrateOffersActivity
import kotlinx.android.synthetic.main.activity_client_proposal_details.*

class ClientProposalDetailsActivity : BaseActivity(), View.OnClickListener {
    private var mileStoneAdapter: MileStoneAdapter? = null
    private var jobsImageAdapter: JobAttachmentsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_proposal_details)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_hire.setOnClickListener(this)
        txt_chat.setOnClickListener(this)
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
            R.id.txt_hire -> {
                startActivity(Intent(this, CrateOffersActivity::class.java))
            }
            R.id.txt_chat -> {
                startActivity(Intent(this, ChatActivity::class.java))
            }
        }
    }
}