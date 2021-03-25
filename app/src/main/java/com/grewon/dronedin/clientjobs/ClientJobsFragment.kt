package com.grewon.dronedin.clientjobs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseFragment
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.clientjobs.active.ClientActiveJobsDetailsActivity
import com.grewon.dronedin.clientjobs.adapter.ActiveJobsAdapter
import com.grewon.dronedin.clientjobs.adapter.HistoryJobsAdapter
import com.grewon.dronedin.clientjobs.adapter.PostedJobsAdapter
import com.grewon.dronedin.clientjobs.contract.ClientJobsContract
import com.grewon.dronedin.clientjobs.history.ClientJobHistoryDetailsActivity
import com.grewon.dronedin.clientjobs.posted.PostedJobDetailsActivity
import com.grewon.dronedin.filter.FilterActivity
import com.grewon.dronedin.helper.AspectImageView
import com.grewon.dronedin.postjob.PostJobActivity
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.JobsDataBean
import com.grewon.dronedin.server.params.GetJobsParams
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.fragment_client_jobs.*
import retrofit2.Retrofit
import javax.inject.Inject


class ClientJobsFragment : BaseFragment(), View.OnClickListener,
    PostedJobsAdapter.OnItemClickListeners, ActiveJobsAdapter.OnItemClickListeners,
    HistoryJobsAdapter.OnItemClickListeners, SwipeRefreshLayout.OnRefreshListener, OnMoreListener,
    ClientJobsContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var clientJobsPresenter: ClientJobsContract.Presenter


    private var activejobsAdapter: ActiveJobsAdapter? = null
    private var postedJobsAdapter: PostedJobsAdapter? = null
    private var historyJobsAdapter: HistoryJobsAdapter? = null
    private var jobStatus = AppConstant.POSTED_JOB_STATUS
    private var pageCount: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_jobs, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        setClicks()
        setLayoutManager()


    }

    private fun setClicks() {
        im_search.setOnClickListener(this)
        fab_add_job.setOnClickListener(this)
    }

    private fun initView() {

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        clientJobsPresenter.attachView(this)
        clientJobsPresenter.attachApiInterface(retrofit)

        title_user_name.text = getString(R.string.hello, preferenceUtils.getLoginCredentials()?.data?.userName)

        segment_group.setOnPositionChangedListener {

            fab_add_job.visibility = if (it == 0) View.VISIBLE else View.GONE

            when (it) {
                0 -> {
                    pageCount = 1
                    jobStatus = AppConstant.POSTED_JOB_STATUS
                    displayProgressView()
                    apiCall()
                }
                1 -> {
                    pageCount = 1
                    jobStatus = AppConstant.ACTIVE_JOB_STATUS
                    displayProgressView()
                    apiCall()
                }
                2 -> {
                    pageCount = 1
                    jobStatus = AppConstant.HISTORY_JOB_STATUS
                    displayProgressView()
                    apiCall()
                }
            }
        }


        apiCall()


    }

    private fun displayProgressView() {
        job_data_recycle.progressView.visibility = View.VISIBLE
        job_data_recycle.emptyView.visibility = View.GONE
        job_data_recycle.recyclerView.visibility = View.GONE
    }

    private fun apiCall() {
        val getJobsParams = GetJobsParams(jobStatus, pageCount)
        clientJobsPresenter.getClientJobs(getJobsParams)

    }

    private fun setLayoutManager() {


        job_data_recycle.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
        )

        job_data_recycle.setRefreshListener(this)
        job_data_recycle.setRefreshingColorResources(
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark
        )
        job_data_recycle.setupMoreListener(this, 1)
    }

    private fun initJobsHistoryAdapter() {
        if (context != null && isVisible) {
            historyJobsAdapter = HistoryJobsAdapter(requireContext(), this)
            job_data_recycle.adapter = historyJobsAdapter
        }

    }

    private fun initActiveJobsAdapter() {
        if (context != null && isVisible) {
            activejobsAdapter = ActiveJobsAdapter(requireContext(), this)
            job_data_recycle.adapter = activejobsAdapter
        }
    }

    private fun initPostedJobsAdapter() {
        if (context != null && isVisible) {
            postedJobsAdapter = PostedJobsAdapter(requireContext(), this)
            job_data_recycle.adapter = postedJobsAdapter
        }
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

    override fun onItemClick(jobsDataBean: JobsDataBean.Data?) {
        startActivityForResult(
            Intent(requireContext(), PostedJobDetailsActivity::class.java).putExtra(
                AppConstant.ID,
                jobsDataBean?.jobId
            ), 12
        )
    }

    override fun onDeleteItem(jobsDataBean: JobsDataBean.Data?, adapterPosition: Int) {
    }

    override fun onHistoryItemClick(jobsDataBean: JobsDataBean.Data?) {
        startActivity(
            Intent(
                requireContext(),
                ClientJobHistoryDetailsActivity::class.java
            ).putExtra(AppConstant.ID, jobsDataBean?.jobId)
        )
    }

    override fun onActiveItemClick(jobsDataBean: JobsDataBean.Data?) {
        startActivity(
            Intent(
                requireContext(),
                ClientActiveJobsDetailsActivity::class.java
            ).putExtra(AppConstant.ID, jobsDataBean?.jobId)
        )
    }

    override fun onRefresh() {
        job_data_recycle.setupMoreListener(this, 1)
        pageCount = 1
        apiCall()
    }

    override fun onMoreAsked(
        overallItemsCount: Int,
        itemsBeforeMore: Int,
        maxLastVisiblePosition: Int
    ) {
        pageCount += 1
        apiCall()
    }

    private fun setEmptyView(errorMessage: String, emptyDrawable: Int?) {
        if (context != null && isVisible) {
            when (jobStatus) {
                AppConstant.POSTED_JOB_STATUS -> {
                    initPostedJobsAdapter()
                }
                AppConstant.ACTIVE_JOB_STATUS -> {
                    initActiveJobsAdapter()
                }
                else -> {
                    initJobsHistoryAdapter()
                }
            }

            if (emptyDrawable != null) {
                val errorImage: AspectImageView =
                    job_data_recycle.emptyView.findViewById(R.id.txt_no_data_image)
                errorImage.setImageResource(emptyDrawable)
            }

            val error: TextView = job_data_recycle.emptyView.findViewById(R.id.txt_no_data_title)
            error.text = errorMessage
        }

    }

    override fun onPostedJobsGetSuccessful(response: JobsDataBean) {
        if (context != null && isVisible) {
            if (response.data != null && response.data.size > 0) {
                if (pageCount == 1) {
                    initPostedJobsAdapter()
                }
                postedJobsAdapter?.addItemsList(response.data)
            } else {
                if (pageCount == 1) {
                    setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
                }
                stopMore()
            }
        }
    }

    override fun onActiveJobsGetSuccessful(response: JobsDataBean) {
        if (context != null && isVisible) {
            if (response.data != null && response.data.size > 0) {
                if (pageCount == 1) {
                    initActiveJobsAdapter()
                }
                activejobsAdapter?.addItemsList(response.data)
            } else {
                if (pageCount == 1) {
                    setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
                }
                stopMore()
            }
        }
    }

    override fun onJobsHistoryGetSuccessful(response: JobsDataBean) {
        if (context != null && isVisible) {
            if (response.data != null && response.data.size > 0) {
                if (pageCount == 1) {
                    initJobsHistoryAdapter()
                }
                historyJobsAdapter?.addItemsList(response.data)
            } else {
                if (pageCount == 1) {
                    setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
                }
                stopMore()
            }
        }
    }

    override fun onJobsGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            setEmptyView(loginParams.msg, R.drawable.ic_no_data)
        }
    }

    override fun onApiException(error: Int) {
        if (pageCount == 1) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 12) {
                pageCount = 1
                jobStatus = AppConstant.POSTED_JOB_STATUS
                displayProgressView()
                apiCall()
            }
        }
    }


    private fun stopMore() {
        if (context != null && isVisible) {
            job_data_recycle.hideMoreProgress()
            job_data_recycle.setupMoreListener(null, 0)
        }
    }


}