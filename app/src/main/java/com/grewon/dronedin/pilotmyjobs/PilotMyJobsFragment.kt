package com.grewon.dronedin.pilotmyjobs

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.invitations.InvitationsDetailActivity
import com.grewon.dronedin.invitations.adapter.InvitationsAdapter
import com.grewon.dronedin.offers.OffersDetailActivity
import com.grewon.dronedin.offers.adapter.OffersAdapter
import com.grewon.dronedin.pilotactivejobs.PilotActiveJobsActivity
import com.grewon.dronedin.pilotfindjobs.FindJobsDetailsActivity
import com.grewon.dronedin.pilotjobhistory.PilotJobHistoryActivity
import com.grewon.dronedin.proposals.ProposalsDetailActivity
import com.grewon.dronedin.proposals.adapter.ProposalsAdapter
import com.grewon.dronedin.server.InvitationsDataBean
import com.grewon.dronedin.server.OffersDataBean
import com.grewon.dronedin.server.ProposalsDataBean
import kotlinx.android.synthetic.main.fragment_pilot_my_jobs.*
import kotlinx.android.synthetic.main.layout_square_toolbar.*


class PilotMyJobsFragment : Fragment(), OffersAdapter.OnItemClickListeners,
    InvitationsAdapter.OnItemClickListeners, ProposalsAdapter.OnItemClickListeners,
    View.OnClickListener {

    private var offersAdapter: OffersAdapter? = null
    private var invitationsAdapter: InvitationsAdapter? = null
    private var proposalsAdapter: ProposalsAdapter? = null

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
        setClicks()
        setOffersAdapter()
        segment_group.setOnPositionChangedListener {
            if (it == 0) {
                setOffersAdapter()
            } else if (it == 1) {
                setInvitationsAdapter()
            } else if (it == 2) {
                setProposalsAdapter()
            }
        }
    }

    private fun setClicks() {
        card_active_jobs.setOnClickListener(this)
        card_job_history.setOnClickListener(this)
    }

    private fun setProposalsAdapter() {
        data_recycle.layoutManager = LinearLayoutManager(context)
        proposalsAdapter = ProposalsAdapter(requireContext(), this)
        data_recycle.adapter = proposalsAdapter
    }

    private fun setInvitationsAdapter() {
        data_recycle.layoutManager = LinearLayoutManager(context)
        invitationsAdapter = InvitationsAdapter(requireContext(), this)
        data_recycle.adapter = invitationsAdapter
    }

    private fun setOffersAdapter() {
        data_recycle.layoutManager = LinearLayoutManager(context)
        offersAdapter = OffersAdapter(requireContext(), this)
        data_recycle.adapter = offersAdapter

    }

    override fun onOffersItemClick(jobsDataBean: OffersDataBean.Result?) {
        startActivity(Intent(context, OffersDetailActivity::class.java))
    }

    override fun onInvitationsItemClick(jobsDataBean: InvitationsDataBean.Result?) {
        startActivity(Intent(context, InvitationsDetailActivity::class.java))
    }

    override fun onProposalsItemClick(jobsDataBean: ProposalsDataBean.Result?) {
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
        }
    }

}