package com.grewon.dronedin.pilotmyjobs

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.grewon.dronedin.earnings.EarningsActivity
import com.grewon.dronedin.helper.AspectImageView
import com.grewon.dronedin.invitations.InvitationsDetailActivity
import com.grewon.dronedin.invitations.adapter.InvitationsAdapter
import com.grewon.dronedin.offers.OffersDetailActivity
import com.grewon.dronedin.offers.adapter.OffersAdapter
import com.grewon.dronedin.pilotactivejobs.PilotActiveJobsActivity
import com.grewon.dronedin.pilotjobhistory.PilotJobHistoryActivity
import com.grewon.dronedin.pilotmyjobs.contract.PilotMyJobsContract
import com.grewon.dronedin.pilotmyjobs.presenter.PilotMyJobsPresenter
import com.grewon.dronedin.proposals.ProposalsDetailActivity
import com.grewon.dronedin.proposals.adapter.ProposalsAdapter
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.InvitationsDataBean
import com.grewon.dronedin.server.OffersDataBean
import com.grewon.dronedin.server.ProposalsDataBean
import com.grewon.dronedin.server.params.GetJobsParams
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.fragment_pilot_my_jobs.*
import kotlinx.android.synthetic.main.layout_square_toolbar.*
import retrofit2.Retrofit
import javax.inject.Inject


class PilotMyJobsFragment : BaseFragment(), OffersAdapter.OnItemClickListeners,
    InvitationsAdapter.OnItemClickListeners, ProposalsAdapter.OnItemClickListeners,
    View.OnClickListener, PilotMyJobsContract.View, SwipeRefreshLayout.OnRefreshListener,
    OnMoreListener {


    @Inject
    lateinit var pilotMyJobsPresenter: PilotMyJobsContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var offersAdapter: OffersAdapter? = null
    private var invitationsAdapter: InvitationsAdapter? = null
    private var proposalsAdapter: ProposalsAdapter? = null

    private var jobStatus = AppConstant.OFFERED_DATA_STATUS
    private var pageCount: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pilot_my_jobs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        txt_toolbar_title.text = getString(R.string.my_jobs)
        initView()
        setClicks()
        segment_group.setOnPositionChangedListener {
            when (it) {
                0 -> {
                    pageCount = 1
                    jobStatus = AppConstant.OFFERED_DATA_STATUS
                    displayProgressView()
                    apiCall()
                }
                1 -> {
                    pageCount = 1
                    jobStatus = AppConstant.INVITATIONS_DATA_STATUS
                    displayProgressView()
                    apiCall()
                }
                2 -> {
                    pageCount = 1
                    jobStatus = AppConstant.PROPOSALS_DATA_STATUS
                    displayProgressView()
                    apiCall()
                }
            }
        }
    }

    private fun setLayoutManager() {


        data_recycle.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
        )

        data_recycle.setRefreshListener(this)
        data_recycle.setRefreshingColorResources(
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark
        )
        data_recycle.setupMoreListener(this, 1)
    }


    private fun initView() {
        setLayoutManager()


        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        pilotMyJobsPresenter.attachView(this)
        pilotMyJobsPresenter.attachApiInterface(retrofit)

        apiCall()

    }

    private fun apiCall() {
        val getJobsParams = GetJobsParams(jobStatus, pageCount)
        if (jobStatus == AppConstant.OFFERED_DATA_STATUS) {
            pilotMyJobsPresenter.getPilotOffersMyJobs(getJobsParams)
        } else if (jobStatus == AppConstant.INVITATIONS_DATA_STATUS) {
            pilotMyJobsPresenter.getPilotInvitationsMyJobs(getJobsParams)
        } else {
            pilotMyJobsPresenter.getPilotProposalsMyJobs(getJobsParams)
        }
    }


    private fun setClicks() {
        card_active_jobs.setOnClickListener(this)
        card_job_history.setOnClickListener(this)
        card_earnings.setOnClickListener(this)
    }

    private fun initProposalsAdapter() {
        proposalsAdapter = ProposalsAdapter(requireContext(), this)
        data_recycle.adapter = proposalsAdapter
    }

    private fun initInvitationsAdapter() {
        invitationsAdapter = InvitationsAdapter(requireContext(), this)
        data_recycle.adapter = invitationsAdapter
    }

    private fun initOffersAdapter() {
        offersAdapter = OffersAdapter(requireContext(), this)
        data_recycle.adapter = offersAdapter

    }


    private fun displayProgressView() {
        data_recycle.progressView.visibility = View.VISIBLE
        data_recycle.emptyView.visibility = View.GONE
        data_recycle.recyclerView.visibility = View.GONE
    }


    override fun onOffersItemClick(jobsDataBean: OffersDataBean.Data?) {
        startActivity(Intent(context, OffersDetailActivity::class.java))
    }

    override fun onInvitationsItemClick(jobsDataBean: InvitationsDataBean.Data?) {
        startActivity(Intent(context, InvitationsDetailActivity::class.java))
    }

    override fun onProposalsItemClick(jobsDataBean: ProposalsDataBean.Data?) {
        startActivity(Intent(context, ProposalsDetailActivity::class.java))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card_active_jobs -> {
                startActivity(Intent(requireContext(), PilotActiveJobsActivity::class.java))
            }
            R.id.card_job_history -> {
                startActivity(Intent(requireContext(), PilotJobHistoryActivity::class.java))
            }
            R.id.card_earnings -> {
                startActivity(Intent(requireContext(), EarningsActivity::class.java))
            }
        }
    }

    override fun onApiException(error: Int) {
        if (pageCount == 1) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }
    }

    override fun onProposalDataGetSuccessful(response: ProposalsDataBean) {
        if (context != null && isVisible) {
            if (response.data != null && response.data.size > 0) {
                if (pageCount == 1) {
                    initProposalsAdapter()
                }
                proposalsAdapter?.addItemsList(response.data)
            } else {
                if (pageCount == 1) {
                    setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
                }
                stopMore()
            }
        }

    }

    override fun onInvitationsDataGetSuccessful(response: InvitationsDataBean) {
        if (context != null && isVisible) {
            if (response.data != null && response.data.size > 0) {
                if (pageCount == 1) {
                    initInvitationsAdapter()
                }
                invitationsAdapter?.addItemsList(response.data)
            } else {
                if (pageCount == 1) {
                    setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
                }
                stopMore()
            }
        }

    }

    override fun onOffersDataGetSuccessful(response: OffersDataBean) {
        if (context != null && isVisible) {
            if (response.data != null && response.data.size > 0) {
                if (pageCount == 1) {
                    initOffersAdapter()
                }
                offersAdapter?.addItemsList(response.data)
            } else {
                if (pageCount == 1) {
                    setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
                }
                stopMore()
            }
        }

    }

    override fun onDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            setEmptyView(loginParams.msg, R.drawable.ic_no_data)
        }
    }

    override fun onRefresh() {
        data_recycle.setupMoreListener(this, 1)
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
                AppConstant.OFFERED_DATA_STATUS -> {
                    initOffersAdapter()
                }
                AppConstant.INVITATIONS_DATA_STATUS -> {
                    initInvitationsAdapter()
                }
                else -> {
                    initProposalsAdapter()
                }
            }

            if (emptyDrawable != null) {
                val errorImage: AspectImageView =
                    data_recycle.emptyView.findViewById(R.id.txt_no_data_image)
                errorImage.setImageResource(emptyDrawable)
            }

            val error: TextView = data_recycle.emptyView.findViewById(R.id.txt_no_data_title)
            error.text = errorMessage
        }

    }


    private fun stopMore() {
        if (context != null && isVisible) {
            data_recycle.hideMoreProgress()
            data_recycle.setupMoreListener(null, 0)
        }
    }


}