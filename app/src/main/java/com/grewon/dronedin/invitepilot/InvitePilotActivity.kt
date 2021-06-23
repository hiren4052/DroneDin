package com.grewon.dronedin.invitepilot

import android.animation.LayoutTransition
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
import com.grewon.dronedin.invitepilot.adapter.InvitePilotAdapter
import com.grewon.dronedin.invitepilot.contract.PilotInviteContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.PilotDataBean
import com.grewon.dronedin.server.params.FilterParams
import com.grewon.dronedin.server.params.PilotInviteParams
import com.grewon.dronedin.utils.ValidationUtils
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.activity_invite_pilot.*
import retrofit2.Retrofit
import java.util.HashMap
import javax.inject.Inject

class InvitePilotActivity : BaseActivity(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener, OnMoreListener, PilotInviteContract.View,
    InvitePilotAdapter.OnItemClickListeners {

    @Inject
    lateinit var pilotInvitePresenter: PilotInviteContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var pageCount: Int = 1

    private var invitePilotAdapter: InvitePilotAdapter? = null
    private var jobId: String = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_pilot)
        setCLicks()
        initView()
    }

    private fun initView() {

        bottom_layout.layoutTransition.enableTransitionType(LayoutTransition.APPEARING)

        jobId = intent.getStringExtra(AppConstant.ID).toString()

        if (ValidationUtils.isEmptyFiled(jobId)) {
            onBackPressed()
        }

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        pilotInvitePresenter.attachView(this)
        pilotInvitePresenter.attachApiInterface(retrofit)

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
        val filterParams = FilterParams(page = pageCount.toString())
        pilotInvitePresenter.getPilotData(filterParams)
    }

    private fun setCLicks() {
        img_back.setOnClickListener(this)
        txt_select_all.setOnClickListener(this)
        txt_invite.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                onBackPressed()
            }
            R.id.txt_select_all -> {
                if (txt_select_all.text == getString(R.string.select_all)) {
                    if (invitePilotAdapter != null && invitePilotAdapter?.itemList?.size!! > 0) {
                        invitePilotAdapter?.setAllSelected()
                        txt_select_all.text = getString(R.string.clear)
                        bottom_layout.visibility = View.VISIBLE
                    }
                } else {
                    if (invitePilotAdapter != null && invitePilotAdapter?.itemList?.size!! > 0) {
                        invitePilotAdapter?.setAllUnSelected()
                        txt_select_all.text = getString(R.string.select_all)
                        bottom_layout.visibility = View.GONE
                    }
                }
            }
            R.id.txt_invite -> {
                if (invitePilotAdapter != null && invitePilotAdapter?.isSelected()!!) {
                    DroneDinApp.loadingDialogMessage = getString(R.string.inviting)
                    val inviteParams =
                        PilotInviteParams(jobId, invitePilotAdapter?.getSelectedPilotsId())
                    pilotInvitePresenter.invitePilots(inviteParams)
                } else {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_select_at_least_one_pilot))
                }
            }
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
        super.onBackPressed()
    }

    override fun onRefresh() {
        pilot_recycle.setupMoreListener(this, 1)
        pageCount = 1
        apiCall(pageCount)
    }


    override fun onApiException(error: Int) {
        if (pageCount == 1) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }
    }

    override fun onPilotDataGetSuccessful(response: PilotDataBean) {
        if (response.data != null && response.data.size > 0) {
            if (pageCount == 1) {
                invitePilotAdapter = InvitePilotAdapter(this, this)
                pilot_recycle.adapter = invitePilotAdapter
            }
            txt_select_all.visibility = View.VISIBLE
            invitePilotAdapter?.addItemsList(response.data)
        } else {
            if (pageCount == 1) {
                txt_select_all.visibility = View.GONE
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

    override fun onPilotInviteSuccessful(response: CommonMessageBean) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)
            onBackPressed()
        }

    }

    override fun onPilotInviteFailed(loginParams: PilotInviteParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onMoreAsked(
        overallItemsCount: Int,
        itemsBeforeMore: Int,
        maxLastVisiblePosition: Int
    ) {
        pageCount += 1

        apiCall(pageCount)

    }


    private fun stopMore() {
        pilot_recycle.hideMoreProgress()
        pilot_recycle.setupMoreListener(null, 0)
    }

    override fun onPilotItemClick(jobsDataBean: PilotDataBean.Data) {
    }

    override fun onLongClick(jobsDataBean: PilotDataBean.Data, adapterPosition: Int) {
        if (invitePilotAdapter != null && invitePilotAdapter?.itemList?.size!! > 0) {
            if (invitePilotAdapter?.isSelected()!!) {
                bottom_layout.visibility = View.VISIBLE
            } else {
                bottom_layout.visibility = View.GONE
            }
        }
    }

    private fun setEmptyView(errorMessage: String, emptyDrawable: Int?) {
        txt_select_all.visibility = View.GONE
        invitePilotAdapter = InvitePilotAdapter(this, this)
        pilot_recycle.adapter = invitePilotAdapter
        if (emptyDrawable != null) {
            val errorImage: AspectImageView =
                pilot_recycle.emptyView.findViewById(R.id.txt_no_data_image)
            errorImage.setImageResource(emptyDrawable)
        }
        val error: TextView = pilot_recycle.emptyView.findViewById(R.id.txt_no_data_title)
        error.text = errorMessage

    }

}