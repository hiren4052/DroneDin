package com.grewon.dronedin.milestone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.pilotfindjobs.adapter.JobsImageAdapter

import kotlinx.android.synthetic.main.activity_milestone_completion_request.*


class MilestoneCompletionRequestActivity : BaseActivity(), View.OnClickListener {

    private var jobsImageAdapter: JobsImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_completion_request)
        initView()
        setClicks()
    }


    private fun initView() {
        DroneDinApp.getAppInstance().loadGifImage(R.drawable.completed_job,top_image)
        setImageAdapter()
    }

    private fun setImageAdapter() {
        image_recycle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = JobsImageAdapter(this)
        image_recycle.adapter = jobsImageAdapter

    }

    private fun setClicks() {
        im_back.setOnClickListener(this)
        txt_accept.setOnClickListener(this)
        txt_reject.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.txt_accept -> {
                finish()
            }
            R.id.txt_reject -> {
                startActivity(Intent(this, MilestoneRejectActivity::class.java))
            }

        }
    }
}