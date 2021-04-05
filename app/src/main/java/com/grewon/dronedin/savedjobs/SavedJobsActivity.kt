package com.grewon.dronedin.savedjobs

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
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.helper.AspectImageView
import com.grewon.dronedin.pilotfindjobs.FindJobsDetailsActivity
import com.grewon.dronedin.pilotfindjobs.adapter.PilotFindJobsAdapter
import com.grewon.dronedin.pilotfindjobs.contract.PilotJobsContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.PilotJobsDataBean
import com.grewon.dronedin.server.params.FilterParams
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.activity_saved_jobs.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject

class SavedJobsActivity : BaseActivity(), PilotJobsContract.View,
    PilotFindJobsAdapter.OnItemClickListeners, OnMoreListener,
    SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Inject
    lateinit var pilotJobsPresenter: PilotJobsContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit


    private var findJobsAdapter: PilotFindJobsAdapter? = null
    private var filterParams: FilterParams? = null
    private var pageCount: Int = 1
    private var adapterPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_jobs)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        txt_toolbar_title.text = getString(R.string.saved_jobs)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        pilotJobsPresenter.attachView(this)
        pilotJobsPresenter.attachApiInterface(retrofit)

        jobs_recycle.setLayoutManager(
            LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
        )

        jobs_recycle.setRefreshListener(this)
        jobs_recycle.setRefreshingColorResources(
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark
        )
        jobs_recycle.setupMoreListener(this, 1)

        apiCall(pageCount)
    }

    private fun apiCall(pageCount: Int) {
        filterParams = FilterParams()
        filterParams?.page = pageCount.toString()
        filterParams?.saved = "1"
        pilotJobsPresenter.getPilotJobs(filterParams!!)

    }


    private fun initJobsAdapter() {
        findJobsAdapter = PilotFindJobsAdapter(this, this)
        jobs_recycle.adapter = findJobsAdapter
    }


    override fun onItemClick(jobsDataBean: PilotJobsDataBean.Data?) {
        startActivity(
            Intent(this, FindJobsDetailsActivity::class.java).putExtra(
                AppConstant.ID,
                jobsDataBean?.jobId
            )
        )
    }

    override fun onJobSaved(jobsDataBean: PilotJobsDataBean.Data?, adapterPosition: Int) {
        this.adapterPosition = adapterPosition
        pilotJobsPresenter.saveJobs(jobsDataBean?.jobId.toString())
    }

    override fun onApiException(error: Int) {
        if (pageCount == 1) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }

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
        jobs_recycle.setupMoreListener(this, 1)
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

    private fun setEmptyView(errorMessage: String, emptyDrawable: Int?) {
        initJobsAdapter()
        if (emptyDrawable != null) {
            val errorImage: AspectImageView =
                jobs_recycle.emptyView.findViewById(R.id.txt_no_data_image)
            errorImage.setImageResource(emptyDrawable)
        }
        val error: TextView =
            jobs_recycle.emptyView.findViewById(R.id.txt_no_data_title)
        error.text = errorMessage

    }

    private fun stopMore() {
        jobs_recycle.hideMoreProgress()
        jobs_recycle.setupMoreListener(null, 0)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }


}