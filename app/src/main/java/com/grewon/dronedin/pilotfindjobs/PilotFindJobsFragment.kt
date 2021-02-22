package com.grewon.dronedin.pilotfindjobs

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.pilotfindjobs.adapter.PilotFindJobsAdapter
import com.grewon.dronedin.server.JobsDataBean
import kotlinx.android.synthetic.main.fragment_pilot_find_jobs.*


class PilotFindJobsFragment : Fragment(), View.OnClickListener,
    PilotFindJobsAdapter.OnItemClickListeners {

    private var findJobsAdapter: PilotFindJobsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pilot_find_jobs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        title_user_name.text = getString(R.string.hello, "Kristen")
        setClicks()
        setJobsAdapter()
    }


    private fun setJobsAdapter() {
        find_job_data_recycle.layoutManager = LinearLayoutManager(context)
        findJobsAdapter = PilotFindJobsAdapter(requireContext(), this)
        find_job_data_recycle.adapter = findJobsAdapter
    }

    private fun setClicks() {

        im_search.setOnClickListener(this)
        image_map.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_search -> {
                startActivity(Intent(context, FindJobsFilterActivity::class.java))
            }
            R.id.image_map -> {

            }

        }
    }

    override fun onItemClick(jobsDataBean: JobsDataBean.Result?) {
        startActivity(Intent(context, FindJobsDetailsActivity::class.java))
    }


}