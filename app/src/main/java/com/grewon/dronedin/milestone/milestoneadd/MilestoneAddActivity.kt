package com.grewon.dronedin.milestone.milestoneadd

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.milestone.adapter.CreateMileStoneAdapter
import com.grewon.dronedin.milestone.milestoneadd.contract.AddMilestoneContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.server.params.AddMileStoneParams
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_milestone_add.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject

class MilestoneAddActivity : BaseActivity(), View.OnClickListener, AddMilestoneContract.View,
    CreateMileStoneAdapter.OnRemoveItemClickListeners {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var addMilestonePresenter: AddMilestoneContract.Presenter

    private var createMileStoneAdapter: CreateMileStoneAdapter? = null

    private var jobId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_add)

        setClicks()
        initView()
    }

    private fun setClicks() {
        txt_send_request.setOnClickListener(this)
        im_add_milestone.setOnClickListener(this)
        txt_send_request.setOnClickListener(this)
        img_back.setOnClickListener(this)
    }

    private fun initView() {

        txt_toolbar_title.text = getString(R.string.add_milestones)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        addMilestonePresenter.attachView(this)
        addMilestonePresenter.attachApiInterface(retrofit)


        create_milestone_recycle.layoutManager = LinearLayoutManager(this)
        createMileStoneAdapter = CreateMileStoneAdapter(this, this)
        create_milestone_recycle.adapter = createMileStoneAdapter


        jobId = intent.getStringExtra(AppConstant.ID).toString()

        if (isPilotAccount()) {
            txt_send_request.text = getString(R.string.send_request)
            text_about_info.text = getString(R.string.milestone_add_pilot_description)
        } else {
            txt_send_request.text = getString(R.string.add_milestones)
            text_about_info.text = getString(R.string.milestone_add_client_description)
        }


    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.im_add_milestone -> {
                when {
                    ValidationUtils.isEmptyFiled(edt_milestone_price.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_milestone_price))
                    }
                    ValidationUtils.isEmptyFiled(edt_milestone_description.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_milestone_description))
                    }
                    else -> {


                        createMileStoneAdapter?.addItems(
                            CreateMilestoneBean(
                                edt_milestone_description.text.toString(),
                                edt_milestone_price.text.toString()
                            )
                        )

                        edt_milestone_price.setText("")
                        edt_milestone_description.setText("")

                    }
                }
            }
            R.id.img_back -> {
                finish()
            }
            R.id.txt_send_request -> {
                if (createMileStoneAdapter != null && createMileStoneAdapter!!.itemList.size == 0) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_create_milestone))
                } else {
                    DroneDinApp.loadingDialogMessage = getString(R.string.adding)
                    val addParams = AddMileStoneParams(jobId, createMileStoneAdapter?.itemList)
                    addMilestonePresenter.addMilestone(addParams)
                }
            }
        }

    }

    override fun onSubmitSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onSubmitFailed(loginParams: AddMileStoneParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onItemRemove(adapterPosition: Int) {

    }

}