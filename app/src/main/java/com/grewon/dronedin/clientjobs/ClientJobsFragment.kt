package com.grewon.dronedin.clientjobs

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.clientjobs.active.ClientActiveJobsDetailsActivity
import com.grewon.dronedin.clientjobs.adapter.ActiveJobsAdapter
import com.grewon.dronedin.clientjobs.adapter.HistoryJobsAdapter
import com.grewon.dronedin.clientjobs.adapter.PostedJobsAdapter
import com.grewon.dronedin.clientjobs.history.ClientJobHistoryDetailsActivity
import com.grewon.dronedin.clientjobs.posted.PostedJobDetailsActivity
import com.grewon.dronedin.filter.FilterActivity
import com.grewon.dronedin.postjob.PostJobActivity
import com.grewon.dronedin.server.JobsDataBean
import kotlinx.android.synthetic.main.fragment_client_jobs.*


class ClientJobsFragment : Fragment(), View.OnClickListener,
    PostedJobsAdapter.OnItemClickListeners, ActiveJobsAdapter.OnItemClickListeners,
    HistoryJobsAdapter.OnItemClickListeners {


    private var activejobsAdapter: ActiveJobsAdapter? = null
    private var postedJobsAdapter: PostedJobsAdapter? = null
    private var historyJobsAdapter: HistoryJobsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_jobs, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        title_user_name.text = getString(R.string.hello, "Kristen")
        setClicks()

        setPostedJobsAdapter()
        segment_group.setOnPositionChangedListener {
            if (it == 0) {
                setPostedJobsAdapter()
                fab_add_job.visibility = View.VISIBLE
            } else if (it == 1) {
                setActiveJobsAdapter()
                fab_add_job.visibility = View.GONE
            } else if (it == 2) {
                setJobsHistoryAdapter()
                fab_add_job.visibility = View.GONE
            }
        }
    }

    private fun setJobsHistoryAdapter() {
        job_data_recycle.layoutManager = LinearLayoutManager(context)
        historyJobsAdapter = HistoryJobsAdapter(requireContext(), this)
        job_data_recycle.adapter = historyJobsAdapter

    }

    private fun setActiveJobsAdapter() {
        job_data_recycle.layoutManager = LinearLayoutManager(context)
        activejobsAdapter = ActiveJobsAdapter(requireContext(), this)
        job_data_recycle.adapter = activejobsAdapter
    }

    private fun setPostedJobsAdapter() {
        job_data_recycle.layoutManager = LinearLayoutManager(context)
        postedJobsAdapter = PostedJobsAdapter(requireContext(), this)
        job_data_recycle.adapter = postedJobsAdapter
    }

    private fun setClicks() {

        im_search.setOnClickListener(this)
        fab_add_job.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_search -> {
                startActivity(Intent(context, FilterActivity::class.java))
            }

            R.id.fab_add_job -> {
                startActivity(Intent(context, PostJobActivity::class.java))
            }

        }
    }

    override fun onItemClick(jobsDataBean: JobsDataBean.Result?) {
        startActivity(Intent(requireContext(), PostedJobDetailsActivity::class.java))
    }

    override fun onDeleteItem(jobsDataBean: JobsDataBean.Result?, adapterPosition: Int) {
    }

    override fun onHistoryItemClick(jobsDataBean: JobsDataBean.Result?) {
        startActivity(Intent(requireContext(), ClientJobHistoryDetailsActivity::class.java))
    }

    override fun onActiveItemClick(jobsDataBean: JobsDataBean.Result?) {
        startActivity(Intent(requireContext(), ClientActiveJobsDetailsActivity::class.java))
    }

}