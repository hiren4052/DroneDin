package com.grewon.dronedin.pilotfindjobs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.mapscreen.JobsMapScreenActivity
import com.grewon.dronedin.pilotfindjobs.adapter.PilotFindJobsAdapter
import com.grewon.dronedin.server.JobsDataBean
import kotlinx.android.synthetic.main.activity_pilot_filter_result.*

class PilotFilterResultActivity : BaseActivity(), PilotFindJobsAdapter.OnItemClickListeners,
    View.OnClickListener {

    private var findJobsAdapter: PilotFindJobsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilot_filter_result)
        setClicks()
        setJobsAdapter()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        img_map_filter.setOnClickListener(this)

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
}