package com.grewon.dronedin.milestone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.milestone.contract.EndProjectContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.CancelMilestoneParams
import kotlinx.android.synthetic.main.activity_end_project.*
import kotlinx.android.synthetic.main.activity_end_project.txt_end_project
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
        txt_end_project.setOnClickListener(this)
        txt_end_forcefully.setOnClickListener(this)
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
            R.id.txt_end_project -> {
                endProjectPresenter.endProject(jobId, "success")
            }
            R.id.img_back -> {
                finish()
            }
            R.id.txt_end_forcefully -> {
                endProjectPresenter.endProject(jobId, "forcely")

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

    override fun onEndFailed(loginParams: CancelMilestoneParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }
}