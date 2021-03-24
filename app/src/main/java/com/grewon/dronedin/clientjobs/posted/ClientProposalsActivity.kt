package com.grewon.dronedin.clientjobs.posted

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.clientjobs.adapter.ClientProposalsAdapter
import com.grewon.dronedin.clientjobs.contract.ClientProposalContract
import com.grewon.dronedin.helper.AspectImageView
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.ProposalsDataBean
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.activity_client_proposals.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject

class ClientProposalsActivity : BaseActivity(), View.OnClickListener,
    ClientProposalsAdapter.OnItemClickListeners, ClientProposalContract.View,
    SwipeRefreshLayout.OnRefreshListener, OnMoreListener {


    @Inject
    lateinit var clientProposalPresenter: ClientProposalContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var clientProposal: ClientProposalsAdapter? = null

    private var pageCount: Int = 1
    private var jobId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_proposals)
        setClicks()
        setLayoutManager()
        initView()
    }

    private fun setLayoutManager() {


        proposal_recycle.setLayoutManager(
            LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
        )

        proposal_recycle.setRefreshListener(this)
        proposal_recycle.setRefreshingColorResources(
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark
        )
        proposal_recycle.setupMoreListener(this, 1)
    }


    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        txt_toolbar_title.text = getString(R.string.proposals)

        jobId = intent.getStringExtra(AppConstant.ID).toString()

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        clientProposalPresenter.attachView(this)
        clientProposalPresenter.attachApiInterface(retrofit)

        apiCall()

    }

    private fun apiCall() {
        clientProposalPresenter.getClientProposals(jobId, pageCount)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }

    override fun onProposalsItemClick(jobsDataBean: ProposalsDataBean.Data?) {
        startActivity(
            Intent(
                this,
                ClientProposalDetailsActivity::class.java
            ).putExtra(AppConstant.ID, jobsDataBean?.proposalId).putExtra(AppConstant.JOB_ID,jobId)
        )
    }


    private fun stopMore() {
        proposal_recycle.hideMoreProgress()
        proposal_recycle.setupMoreListener(null, 0)
    }

    override fun onProposalGetSuccessful(response: ProposalsDataBean) {
        if (response.data != null && response.data.size > 0) {
            if (pageCount == 1) {
                initProposalsAdapter()
            }
            clientProposal?.addItemsList(response.data)
        } else {
            if (pageCount == 1) {
                setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
            }
            stopMore()
        }
    }

    private fun initProposalsAdapter() {
        clientProposal = ClientProposalsAdapter(this, this)
        proposal_recycle.adapter = clientProposal
    }

    override fun onProposalGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            setEmptyView(loginParams.msg, R.drawable.ic_no_data)
        }
    }

    override fun onApiException(error: Int) {
        if (pageCount == 1) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }
    }

    private fun setEmptyView(errorMessage: String, emptyDrawable: Int?) {
        initProposalsAdapter()
        if (emptyDrawable != null) {
            val errorImage: AspectImageView =
                proposal_recycle.emptyView.findViewById(R.id.txt_no_data_image)
            errorImage.setImageResource(emptyDrawable)
        }

        val error: TextView = proposal_recycle.emptyView.findViewById(R.id.txt_no_data_title)
        error.text = errorMessage
    }

    override fun onRefresh() {
        proposal_recycle.setupMoreListener(this, 1)
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


}