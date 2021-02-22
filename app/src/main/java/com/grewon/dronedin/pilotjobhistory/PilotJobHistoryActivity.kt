package com.grewon.dronedin.pilotjobhistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.pilotactivejobs.adapter.PilotActiveJobsAdapter
import com.grewon.dronedin.pilotjobhistory.adapter.PilotJobsHistoryAdapter
import com.grewon.dronedin.server.JobsDataBean
import kotlinx.android.synthetic.main.activity_pilot_job_history.*

class PilotJobHistoryActivity : BaseActivity(), View.OnClickListener,
    PilotJobsHistoryAdapter.OnItemClickListeners {

    private var pilotJobsHistoryAdapter: PilotJobsHistoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilot_job_history)
        setClicks()
        initView()
    }

    private fun initView() {
        data_recycle.layoutManager = LinearLayoutManager(this)
        pilotJobsHistoryAdapter = PilotJobsHistoryAdapter(this, this)
        data_recycle.adapter = pilotJobsHistoryAdapter
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

    override fun onJobsHistoryItemClick(jobsDataBean: JobsDataBean.Result?) {

    }
}