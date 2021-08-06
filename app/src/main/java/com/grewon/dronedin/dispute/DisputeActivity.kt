package com.grewon.dronedin.dispute

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
import com.grewon.dronedin.dispute.adapter.DisputeAdapter
import com.grewon.dronedin.dispute.contract.DisputeContract
import com.grewon.dronedin.helper.AspectImageView
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.DisputeBean
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.activity_dispute.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.img_back
import retrofit2.Retrofit
import javax.inject.Inject

class DisputeActivity : BaseActivity(), View.OnClickListener, DisputeContract.View,
    SwipeRefreshLayout.OnRefreshListener, OnMoreListener, DisputeAdapter.OnItemClickListeners {

    private var pageCount: Int = 1

    private var disputeAdapter: DisputeAdapter? = null

    @Inject
    lateinit var disputePresenter: DisputeContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispute)
        setLayoutManager()
        setClicks()
        initView()
    }

    private fun initView() {

        txt_toolbar_title.text = getString(R.string.dispute_resolution_center)
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        disputePresenter.attachView(this)
        disputePresenter.attachApiInterface(retrofit)

        apiCall()
    }

    private fun apiCall() {
        disputePresenter.getDispute(pageCount)

    }

    private fun initAdapter() {

        disputeAdapter = DisputeAdapter(this, isPilotAccount(), this)
        dispute_recycle.adapter = disputeAdapter

    }

    private fun setLayoutManager() {

        dispute_recycle.setLayoutManager(
            LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
        )

        dispute_recycle.setRefreshListener(this)

        dispute_recycle.setRefreshingColorResources(
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark
        )

        dispute_recycle.setupMoreListener(this, 1)
    }


    private fun setClicks() {

        img_back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }

    override fun onApiException(error: Int) {
        if (pageCount == 1) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }
    }

    override fun onDisputeDataGetSuccessful(response: DisputeBean) {
        if (response.data != null && response.data.size > 0) {
            if (pageCount == 1) {
                initAdapter()
            }
            disputeAdapter?.addItemsList(response.data)
        } else {
            if (pageCount == 1) {
                setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
            }
            stopMore()
        }
    }

    override fun onDisputeDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            if (pageCount == 1) {
                setEmptyView(loginParams.msg, R.drawable.ic_no_data)
            }
            stopMore()
        }
    }

    override fun onRefresh() {
        dispute_recycle.setupMoreListener(this, 1)
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
        dispute_recycle.hideMoreProgress()
        dispute_recycle.setupMoreListener(null, 0)
    }

    private fun setEmptyView(errorMessage: String, emptyDrawable: Int?) {
        initAdapter()
        if (emptyDrawable != null) {
            val errorImage: AspectImageView =
                dispute_recycle.emptyView.findViewById(R.id.txt_no_data_image)
            errorImage.setImageResource(emptyDrawable)
        }

        val error: TextView = dispute_recycle.emptyView.findViewById(R.id.txt_no_data_title)
        error.text = errorMessage
    }

    override fun onDisputeItemClick(jobsDataBean: DisputeBean.Data?) {
        startActivity(
            Intent(this, DisputeDetailsActivity::class.java).putExtra(
                AppConstant.ID,
                jobsDataBean?.disputeId
            )
        )
    }

}