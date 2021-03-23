package com.grewon.dronedin.clientjobs.posted

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.clientjobs.adapter.ClientProposalsAdapter
import com.grewon.dronedin.server.ProposalsDataBean
import kotlinx.android.synthetic.main.activity_client_proposals.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class ClientProposalsActivity : BaseActivity(), View.OnClickListener,
    ClientProposalsAdapter.OnItemClickListeners {
    private var clientProposal: ClientProposalsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_proposals)
        setClicks()
        initView()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        txt_toolbar_title.text = getString(R.string.proposals)


        proposal_recycle.layoutManager = LinearLayoutManager(this)
        clientProposal = ClientProposalsAdapter(this, this)
        proposal_recycle.adapter = clientProposal
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }

    override fun onProposalsItemClick(jobsDataBean: ProposalsDataBean.Data?) {
        startActivity(Intent(this, ClientProposalDetailsActivity::class.java))
    }
}