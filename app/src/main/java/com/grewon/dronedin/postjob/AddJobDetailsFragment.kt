package com.grewon.dronedin.postjob

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grewon.dronedin.R
import com.grewon.dronedin.pilotfindjobs.adapter.JobsImageAdapter
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.server.JobsImageDataBean
import com.grewon.dronedin.submitproposal.adapter.CreateMileStoneAdapter
import kotlinx.android.synthetic.main.fragment_add_job_details.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*


class AddJobDetailsFragment : Fragment(), View.OnClickListener {

    private var createMileStoneAdapter: CreateMileStoneAdapter? = null
    private var jobsImageAdapter: JobsImageAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_job_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setClicks()
        initView()
    }

    private fun initView() {
        milestone_recycle.layoutManager = LinearLayoutManager(context)
        createMileStoneAdapter = CreateMileStoneAdapter(requireContext())
        milestone_recycle.adapter = createMileStoneAdapter
        createMileStoneAdapter?.addItems(CreateMilestoneBean())

        setImageAdapter()

    }

    private fun setImageAdapter() {
        image_recycle.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        jobsImageAdapter = JobsImageAdapter(requireContext())
        image_recycle.adapter = jobsImageAdapter

    }

    private fun setClicks() {
        im_add_milestone.setOnClickListener(this)
        im_add_attachments.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.im_add_milestone -> {
                createMileStoneAdapter?.addItems(CreateMilestoneBean())
            }
            R.id.im_add_attachments -> {
                jobsImageAdapter?.addItems(JobsImageDataBean.Result())
            }
        }
    }

}