package com.grewon.dronedin.clientjobs.clientoffers

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
import com.grewon.dronedin.clientjobs.adapter.ClientOffersAdapter
import com.grewon.dronedin.clientjobs.contract.ClientOffersContract
import com.grewon.dronedin.clientjobs.presenter.ClientOffersPresenter
import com.grewon.dronedin.helper.AspectImageView
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.OffersDataBean
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.activity_client_offers.*
import kotlinx.android.synthetic.main.activity_client_proposals.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject

class ClientOffersActivity : BaseActivity(), View.OnClickListener,
    ClientOffersAdapter.OnItemClickListeners, ClientOffersContract.View, OnMoreListener,
    SwipeRefreshLayout.OnRefreshListener {

    private var clientOffersAdapter: ClientOffersAdapter? = null

    @Inject
    lateinit var clientOffersPresenter: ClientOffersContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit


    private var pageCount: Int = 1
    private var jobId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_offers)
        setClicks()
        setLayoutManager()
        initView()
    }

    private fun setLayoutManager() {


        offers_recycle.setLayoutManager(
            LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
        )

        offers_recycle.setRefreshListener(this)
        offers_recycle.setRefreshingColorResources(
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark
        )
        offers_recycle.setupMoreListener(this, 1)
    }


    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        txt_toolbar_title.text = getString(R.string.offers)


        jobId = intent.getStringExtra(AppConstant.ID).toString()

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        clientOffersPresenter.attachView(this)
        clientOffersPresenter.attachApiInterface(retrofit)

        apiCall()


    }

    private fun apiCall() {
        clientOffersPresenter.getClientOffers(jobId, pageCount)
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }

    private fun initAdapter() {
        clientOffersAdapter = ClientOffersAdapter(this, this)
        offers_recycle.adapter = clientOffersAdapter
    }


    override fun onOffersItemClick(jobsDataBean: OffersDataBean.Data?) {
        startActivityForResult(
            Intent(this, ClientOffersDetailsActivity::class.java).putExtra(
                AppConstant.ID,
                jobsDataBean?.offerId
            ), 12
        )
    }

    override fun onOffersGetSuccessful(response: OffersDataBean) {
        if (response.data != null && response.data.size > 0) {
            if (pageCount == 1) {
                initAdapter()
            }
            clientOffersAdapter?.addItemsList(response.data)
        } else {
            if (pageCount == 1) {
                setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
            }
            stopMore()
        }
    }

    override fun onOffersGetFailed(loginParams: CommonMessageBean) {
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
        initAdapter()
        if (emptyDrawable != null) {
            val errorImage: AspectImageView =
                offers_recycle.emptyView.findViewById(R.id.txt_no_data_image)
            errorImage.setImageResource(emptyDrawable)
        }

        val error: TextView = offers_recycle.emptyView.findViewById(R.id.txt_no_data_title)
        error.text = errorMessage
    }


    override fun onRefresh() {
        offers_recycle.setupMoreListener(this, 1)
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
        offers_recycle.hideMoreProgress()
        offers_recycle.setupMoreListener(null, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 12) {
                pageCount = 1
                apiCall()
            }
        }
    }

}