package com.grewon.dronedin.pilotactivejobs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.pilotactivejobs.adapter.PilotActiveJobsAdapter
import com.grewon.dronedin.server.JobsDataBean
import com.grewon.dronedin.server.OffersDataBean
import kotlinx.android.synthetic.main.activity_pilot_active_jobs.*
import kotlinx.android.synthetic.main.activity_pilot_active_jobs.data_recycle
import kotlinx.android.synthetic.main.fragment_pilot_my_jobs.*

class PilotActiveJobsActivity : BaseActivity(), PilotActiveJobsAdapter.OnItemClickListeners,
    View.OnClickListener {
    private var pilotActiveJobsAdapter: PilotActiveJobsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilot_active_jobs)
        setClicks()
        initView()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        data_recycle.layoutManager = LinearLayoutManager(this)
        pilotActiveJobsAdapter = PilotActiveJobsAdapter(this, this)
        data_recycle.adapter = pilotActiveJobsAdapter
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }

    }

    override fun onActiveItemClick(jobsDataBean: JobsDataBean.Result?) {
        startActivity(Intent(this, PilotActiveJobsDetailActivity::class.java))
    }
}

