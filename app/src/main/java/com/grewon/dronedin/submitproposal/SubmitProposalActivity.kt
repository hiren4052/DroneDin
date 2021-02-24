package com.grewon.dronedin.submitproposal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.pilotfindjobs.adapter.JobsImageAdapter
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.server.JobsImageDataBean
import com.grewon.dronedin.submitproposal.adapter.CreateMileStoneAdapter
import kotlinx.android.synthetic.main.activity_submit_proposal.*
import kotlinx.android.synthetic.main.activity_submit_proposal.image_recycle
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.img_back

class SubmitProposalActivity : BaseActivity(), View.OnClickListener {
    private var createMileStoneAdapter: CreateMileStoneAdapter? = null
    private var jobsImageAdapter: JobsImageAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_proposal)
        txt_toolbar_title.text = getString(R.string.submit_proposal)
        setClicks()
        initView()
    }

    private fun initView() {
        milestone_recycle.layoutManager = LinearLayoutManager(this)
        createMileStoneAdapter = CreateMileStoneAdapter(this)
        milestone_recycle.adapter = createMileStoneAdapter
        createMileStoneAdapter?.addItems(CreateMilestoneBean())

        setImageAdapter()

    }

    private fun setImageAdapter() {
        image_recycle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = JobsImageAdapter(this)
        image_recycle.adapter = jobsImageAdapter

    }

    private fun setClicks() {
        im_add_milestone.setOnClickListener(this)
        img_back.setOnClickListener(this)
        im_add_attachments.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.im_add_milestone -> {
                createMileStoneAdapter?.addItems(CreateMilestoneBean())
            }
            R.id.im_add_attachments -> {
                jobsImageAdapter?.addItems(JobsImageDataBean.Result())
            }
        }
    }
}