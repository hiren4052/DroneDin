package com.grewon.dronedin.milestone.milestoneaddrequest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.milestone.adapter.MileStoneBlueBackgroundAdapter
import com.grewon.dronedin.milestone.milestoneaddrequest.contract.NewMilestoneRequestContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.MilestonesDataBean
import com.grewon.dronedin.server.NewMilestoneRequest
import com.grewon.dronedin.server.params.NewMilestoneStatusUpdateParams
import kotlinx.android.synthetic.main.activity_milestone_add_request.*


import retrofit2.Retrofit
import javax.inject.Inject


class MilestoneAddRequestActivity : BaseActivity(), View.OnClickListener,
    NewMilestoneRequestContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var newMilestoneRequestPresenter: NewMilestoneRequestContract.Presenter

    private var mileStoneAdapter: MileStoneBlueBackgroundAdapter? = null

    private var milestoneBean: NewMilestoneRequest? = null
    private var milestoneRequestId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_add_request)
        initView()
        setClicks()
    }

    override fun onResume() {
        super.onResume()

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        newMilestoneRequestPresenter.attachView(this)
        newMilestoneRequestPresenter.attachApiInterface(retrofit)

        newMilestoneRequestPresenter.getNewMilestoneDetail(milestoneRequestId)

    }


    private fun initView() {

        milestoneRequestId = intent.getStringExtra(AppConstant.ID).toString()
    }

    private fun setMileStoneAdapter(milestoneList: ArrayList<MilestonesDataBean>) {
        mile_stone_recycle.layoutManager = LinearLayoutManager(this)
        mileStoneAdapter = MileStoneBlueBackgroundAdapter(this)
        mile_stone_recycle.adapter = mileStoneAdapter
        mileStoneAdapter?.addItemsList(milestoneList)
    }

    private fun setClicks() {
        im_back.setOnClickListener(this)
        txt_accept.setOnClickListener(this)
        txt_reject.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.txt_accept -> {
                val params = NewMilestoneStatusUpdateParams()
                params.milestoneRequestId = milestoneRequestId
                params.milestoneRequestStatus = "accept"
                newMilestoneRequestPresenter.milestoneStatusUpdate(params)
            }
            R.id.txt_reject -> {
                startActivityForResult(
                    Intent(
                        this,
                        MilestoneAddRejectActivity::class.java
                    ).putExtra(AppConstant.ID, milestoneRequestId), 11
                )
            }

        }
    }

    override fun onNewMilestoneGetSuccessFully(loginParams: NewMilestoneRequest) {
        setView(loginParams)
    }

    private fun setView(response: NewMilestoneRequest) {
        milestoneBean = response


        if (response.milestone != null && response.milestone.size > 0) {
            mile_stone_recycle.visibility = View.VISIBLE
            val milestoneList = ArrayList<MilestonesDataBean>()
            for (item in response.milestone) {
                val milesStone = MilestonesDataBean()
                milesStone.milestoneDetails = item.milestoneDetails
                milesStone.milestonePrice = item.milestonePrice
                milesStone.milestoneId = item.milestoneId
                milestoneList.add(milesStone)
            }
            setMileStoneAdapter(milestoneList)
        } else {
            mile_stone_recycle.visibility = View.GONE
        }


    }

    override fun onNewMilestoneGetFailed(loginParams: CommonMessageBean) {
        finish()
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
        finish()
    }

    override fun onNewMilestoneStatusSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }

    }

    override fun onNewMilestoneStatusFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 11) {
                setResult(RESULT_OK)
                finish()
            }
        }
    }


}