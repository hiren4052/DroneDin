package com.grewon.dronedin.pilotjobhistory

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
import com.grewon.dronedin.helper.AspectImageView
import com.grewon.dronedin.pilotjobhistory.adapter.PilotJobsHistoryAdapter
import com.grewon.dronedin.pilotjobhistory.contract.PilotHistoryJobsContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.PilotJobHistoryBean
import com.grewon.dronedin.server.params.GetJobsParams
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.activity_pilot_job_history.*
import retrofit2.Retrofit
import javax.inject.Inject

class PilotJobHistoryActivity : BaseActivity(), View.OnClickListener,
    PilotJobsHistoryAdapter.OnItemClickListeners, PilotHistoryJobsContract.View, OnMoreListener,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var pilotHistoryJobsPresenter: PilotHistoryJobsContract.Presenter

    private var pilotJobsHistoryAdapter: PilotJobsHistoryAdapter? = null

    private var pageCount: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilot_job_history)
        setClicks()
        setLayoutManager()
        initView()
    }

    private fun setLayoutManager() {

        data_recycle.setLayoutManager(
            LinearLayoutManager(
                this,
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

    private fun apiCall() {

        val params = GetJobsParams(AppConstant.HISTORY_JOB_STATUS, pageCount)
        pilotHistoryJobsPresenter.getPilotHistory(params)

    }

    private fun initView() {

        DroneDinApp.getAppInstance().getAppComponent().inject(this)

        pilotHistoryJobsPresenter.attachView(this)
        pilotHistoryJobsPresenter.attachApiInterface(retrofit)

        apiCall()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initAdapter() {

        pilotJobsHistoryAdapter = PilotJobsHistoryAdapter(this, this)
        data_recycle.adapter = pilotJobsHistoryAdapter

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }

    override fun onJobsHistoryItemClick(jobsDataBean: PilotJobHistoryBean.Data.History) {
        startActivity(
            Intent(this, PilotHistoryDetailsActivity::class.java).putExtra(
                AppConstant.ID,
                jobsDataBean.offerId
            )
        )
    }

    override fun onApiException(error: Int) {
        if (pageCount == 1) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }
    }

    override fun onHistoryDataGetSuccessful(response: PilotJobHistoryBean) {
        if (response.data?.history != null && response.data.history.size > 0) {
            if (pageCount == 1) {
                txt_active_job.text = response.data?.count?.active.toString()
                txt_completed_job.text = response.data?.count?.completed.toString()
                txt_cancelled_job.text = response.data?.count?.cancelled.toString()
                initAdapter()
            }
            pilotJobsHistoryAdapter?.addItemsList(response.data.history)
        } else {
            if (pageCount == 1) {
                txt_active_job.text = response.data?.count?.active.toString()
                txt_completed_job.text = response.data?.count?.completed.toString()
                txt_cancelled_job.text = response.data?.count?.cancelled.toString()
                setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
            }
            stopMore()
        }
    }

    override fun onHistoryDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            if (pageCount == 1) {
                setEmptyView(loginParams.msg, R.drawable.ic_no_data)
                stopMore()
            }
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

    private fun stopMore() {
        data_recycle.hideMoreProgress()
        data_recycle.setupMoreListener(null, 0)
    }

    private fun setEmptyView(errorMessage: String, emptyDrawable: Int?) {
        initAdapter()
        if (emptyDrawable != null) {
            val errorImage: AspectImageView =
                data_recycle.emptyView.findViewById(R.id.txt_no_data_image)
            errorImage.setImageResource(emptyDrawable)
        }

        val error: TextView = data_recycle.emptyView.findViewById(R.id.txt_no_data_title)
        error.text = errorMessage
    }

}