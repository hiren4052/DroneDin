package com.grewon.dronedin.filter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.clientjobs.adapter.FindPilotAdapter
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.filter.contract.FilterContract
import com.grewon.dronedin.helper.AspectImageView
import com.grewon.dronedin.mapscreen.JobsMapScreenActivity
import com.grewon.dronedin.pilotfindjobs.FindJobsDetailsActivity
import com.grewon.dronedin.pilotfindjobs.adapter.PilotFindJobsAdapter
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.PilotDataBean
import com.grewon.dronedin.server.PilotJobsDataBean
import com.grewon.dronedin.server.params.FilterParams
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.activity_filter_result.*
import retrofit2.Retrofit
import javax.inject.Inject

class FilterResultActivity : BaseActivity(), PilotFindJobsAdapter.OnItemClickListeners,
    View.OnClickListener, FindPilotAdapter.OnItemClickListeners, FilterContract.View,
    SwipeRefreshLayout.OnRefreshListener, OnMoreListener {

    @Inject
    lateinit var filterPresenter: FilterContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var findJobsAdapter: PilotFindJobsAdapter? = null

    private var findPilotAdapter: FindPilotAdapter? = null

    private var filterParams: FilterParams? = null
    private var adapterPosition: Int = 0

    private var pageCount: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_result)
        initView()
        setClicks()


    }

    private fun initView() {

        filterParams = intent.getParcelableExtra(AppConstant.BEAN)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        filterPresenter.attachView(this)
        filterPresenter.attachApiInterface(retrofit)

        pilot_recycle.setLayoutManager(
            LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
        )

        pilot_recycle.setRefreshListener(this)
        pilot_recycle.setRefreshingColorResources(
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark
        )
        pilot_recycle.setupMoreListener(this, 1)

        apiCall(pageCount)

    }

    private fun apiCall(pageCount: Int) {
        if (isPilotAccount()) {
            filterParams?.page = pageCount.toString()
            filterPresenter.getPilotJobs(filterParams!!)
        } else {
            filterParams?.page = pageCount.toString()
            filterPresenter.getPilotData(filterParams!!)
        }
    }


    private fun setClicks() {
        img_back.setOnClickListener(this)
        img_map_filter.setOnClickListener(this)

    }


    private fun initJobsAdapter() {
        findJobsAdapter = PilotFindJobsAdapter(this, this)
        pilot_recycle.adapter = findJobsAdapter
    }

    override fun onItemClick(jobsDataBean: PilotJobsDataBean.Data?) {
        startActivity(Intent(this, FindJobsDetailsActivity::class.java).putExtra(
            AppConstant.ID,
            jobsDataBean?.jobId
        ))
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }

            R.id.img_map_filter -> {
                startActivity(
                    Intent(
                        this,
                        JobsMapScreenActivity::class.java
                    ).putExtra(AppConstant.BEAN, filterParams)
                )
            }

        }
    }


    override fun onApiException(error: Int) {
        if (pageCount == 1) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }
    }

    override fun onPilotDataGetSuccessful(response: PilotDataBean) {
        if (response.data != null && response.data.size > 0) {
            if (pageCount == 1) {
                initPilotAdapter()
            }
            findPilotAdapter?.addItemsList(response.data)
        } else {
            if (pageCount == 1) {
                setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
            }
            stopMore()
        }
    }

    override fun onPilotDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            setEmptyView(loginParams.msg, R.drawable.ic_no_data)
        }
    }

    override fun onPilotSaveSuccessful(response: CommonMessageBean) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)
            if (findPilotAdapter != null)
                findPilotAdapter?.notifySavedItem(adapterPosition)
        }

    }

    override fun onPilotSaveFailed(loginParams: CommonMessageBean) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onJobsDataGetSuccessful(response: PilotJobsDataBean) {
        if (response.data != null && response.data.size > 0) {
            if (pageCount == 1) {
                initJobsAdapter()
            }
            findJobsAdapter?.addItemsList(response.data)
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

    override fun onJobsSaveSuccessful(response: CommonMessageBean) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)
            if (findJobsAdapter != null)
                findJobsAdapter?.notifySavedItem(adapterPosition)
        }
    }

    override fun onJobsSaveFailed(loginParams: CommonMessageBean) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onRefresh() {
        pilot_recycle.setupMoreListener(this, 1)
        pageCount = 1
        apiCall(pageCount)
    }

    override fun onMoreAsked(
        overallItemsCount: Int,
        itemsBeforeMore: Int,
        maxLastVisiblePosition: Int
    ) {
        pageCount += 1

        apiCall(pageCount)
    }

    override fun onPilotItemClick(jobsDataBean: PilotDataBean.Data?) {

    }

    override fun onPilotSaved(jobsDataBean: PilotDataBean.Data?, adapterPosition: Int) {
        this.adapterPosition = adapterPosition
        filterPresenter.savePilots(jobsDataBean?.userId.toString())
    }

    override fun onJobSaved(jobsDataBean: PilotJobsDataBean.Data?, adapterPosition: Int) {
        this.adapterPosition = adapterPosition
        filterPresenter.saveJobs(jobsDataBean?.jobId.toString())
    }


    private fun stopMore() {
        pilot_recycle.hideMoreProgress()
        pilot_recycle.setupMoreListener(null, 0)
    }


    private fun setEmptyView(errorMessage: String, emptyDrawable: Int?) {
        initPilotAdapter()
        if (emptyDrawable != null) {
            val errorImage: AspectImageView =
                pilot_recycle.emptyView.findViewById(R.id.txt_no_data_image)
            errorImage.setImageResource(emptyDrawable)
        }
        val error: TextView = pilot_recycle.emptyView.findViewById(R.id.txt_no_data_title)
        error.text = errorMessage

    }

    private fun initPilotAdapter() {
        findPilotAdapter = FindPilotAdapter(this, this)
        pilot_recycle.adapter = findPilotAdapter
    }

}