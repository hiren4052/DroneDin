package com.grewon.dronedin.project.endproject

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.enum.JOB_REQUEST_TYPE
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.project.endproject.contract.EndProjectContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.CancelEndProjectParams
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_end_project.edt_description
import kotlinx.android.synthetic.main.activity_end_project.txt_cancel_project
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject

class EndProjectActivity : BaseActivity(), View.OnClickListener, EndProjectContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var endProjectPresenter: EndProjectContract.Presenter

    private var jobId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_project)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_cancel_project.setOnClickListener(this)
    }

    private fun initView() {
        txt_toolbar_title.text = getString(R.string.end_complete_project)
        jobId = intent.getStringExtra(AppConstant.ID).toString()

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        endProjectPresenter.attachView(this)
        endProjectPresenter.attachApiInterface(retrofit)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> {
                finish()
            }
            R.id.txt_cancel_project -> {
                if (ValidationUtils.isEmptyFiled(edt_description.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_description))
                } else {
                    val params = CancelEndProjectParams()
                    params.jobId=jobId
                    params.requestType=JOB_REQUEST_TYPE.end.name
                    params.requestNote=edt_description.text.toString()
                    endProjectPresenter.endProject(params)
                }

            }
        }
    }

    override fun onEndSuccessFully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onEndFailed(loginParams: CancelEndProjectParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }
}