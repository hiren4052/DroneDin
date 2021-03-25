package com.grewon.dronedin.pilotactivejobs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import com.grewon.dronedin.pilotactivejobs.adapter.PilotActiveJobsAdapter
import com.grewon.dronedin.pilotactivejobs.contract.PilotActiveJobsContract
import com.grewon.dronedin.pilotactivejobs.presenter.PilotActiveJobsPresenter
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.JobsDataBean
import com.grewon.dronedin.server.OffersDataBean
import com.grewon.dronedin.server.PilotActiveJobsData
import com.grewon.dronedin.server.params.GetJobsParams
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.activity_client_proposals.*
import kotlinx.android.synthetic.main.activity_pilot_active_jobs.*
import kotlinx.android.synthetic.main.activity_pilot_active_jobs.data_recycle
import kotlinx.android.synthetic.main.fragment_pilot_my_jobs.*
import retrofit2.Retrofit
import javax.inject.Inject

class PilotActiveJobsActivity : BaseActivity(), PilotActiveJobsAdapter.OnItemClickListeners,
    View.OnClickListener, PilotActiveJobsContract.View, OnMoreListener,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var pilotActiveJobsPresenter: PilotActiveJobsContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var pilotActiveJobsAdapter: PilotActiveJobsAdapter? = null

    private var pageCount: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilot_active_jobs)
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

        val params = GetJobsParams(AppConstant.ACTIVE_JOB_STATUS, pageCount)
        pilotActiveJobsPresenter.getPilotActiveJobs(params)
    }


    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        DroneDinApp.getAppInstance().getAppComponent().inject(this)

        pilotActiveJobsPresenter.attachView(this)
        pilotActiveJobsPresenter.attachApiInterface(retrofit)
        apiCall()

    }

    private fun initAdapter() {
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

    override fun onActiveItemClick(jobsDataBean: PilotActiveJobsData.Data?) {
        startActivity(
            Intent(
                this,
                PilotActiveJobsDetailActivity::class.java
            ).putExtra(AppConstant.ID, jobsDataBean?.offerId)
        )
    }

    override fun onApiException(error: Int) {
        if (pageCount == 1) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }
    }

    override fun onJobsDataGetSuccessful(response: PilotActiveJobsData) {
        if (response.data != null && response.data.size > 0) {
            if (pageCount == 1) {
                initAdapter()
            }
            pilotActiveJobsAdapter?.addItemsList(response.data)
        } else {
            if (pageCount == 1) {
                setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
            }
            stopMore()
        }
    }

    override fun onJobsDataGetFailed(loginParams: CommonMessageBean) {
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

