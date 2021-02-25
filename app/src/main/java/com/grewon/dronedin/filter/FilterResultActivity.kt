package com.grewon.dronedin.filter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.clientjobs.adapter.FindPilotAdapter
import com.grewon.dronedin.mapscreen.JobsMapScreenActivity
import com.grewon.dronedin.pilotfindjobs.FindJobsDetailsActivity
import com.grewon.dronedin.pilotfindjobs.adapter.PilotFindJobsAdapter
import com.grewon.dronedin.server.JobsDataBean
import kotlinx.android.synthetic.main.activity_filter_result.*

class FilterResultActivity : BaseActivity(), PilotFindJobsAdapter.OnItemClickListeners,
    View.OnClickListener, FindPilotAdapter.OnItemClickListeners {

    private var findJobsAdapter: PilotFindJobsAdapter? = null
    private var findPilotAdapter: FindPilotAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_result)
        setClicks()
        if(isPilotAccount()) {
            setJobsAdapter()
        }else{
            setPilotAdapter()
        }
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        img_map_filter.setOnClickListener(this)

    }


    private fun setPilotAdapter() {
        find_job_data_recycle.layoutManager = LinearLayoutManager(this)
        findPilotAdapter = FindPilotAdapter(this, this)
        find_job_data_recycle.adapter = findPilotAdapter
    }

    private fun setJobsAdapter() {
        find_job_data_recycle.layoutManager = LinearLayoutManager(this)
        findJobsAdapter = PilotFindJobsAdapter(this, this)
        find_job_data_recycle.adapter = findJobsAdapter
    }

    override fun onItemClick(jobsDataBean: JobsDataBean.Result?) {
        startActivity(Intent(this, FindJobsDetailsActivity::class.java))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }

            R.id.img_map_filter -> {
                startActivity(Intent(this, JobsMapScreenActivity::class.java))
            }

        }
    }

    override fun onPilotItemClick(jobsDataBean: JobsDataBean.Result?) {

    }
}