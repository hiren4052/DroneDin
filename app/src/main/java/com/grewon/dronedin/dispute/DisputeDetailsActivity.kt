package com.grewon.dronedin.dispute

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.dispute.contract.DisputeDetailsContract
import com.grewon.dronedin.enum.DISPUTE_STATUS
import com.grewon.dronedin.server.*
import com.grewon.dronedin.utils.TimeUtils
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_dispute_details.*
import kotlinx.android.synthetic.main.layout_no_data.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject

class DisputeDetailsActivity : BaseActivity(), View.OnClickListener, DisputeDetailsContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var disputeDetailsPresenter: DisputeDetailsContract.Presenter


    private var disputeId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispute_details)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_message.setOnClickListener(this)

    }

    private fun initView() {

        txt_toolbar_title.text = getString(R.string.dispute_details)

        disputeId = intent.getStringExtra(AppConstant.ID).toString()


        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        disputeDetailsPresenter.attachView(this)
        disputeDetailsPresenter.attachApiInterface(retrofit)

        disputeDetailsPresenter.getDisputeDetails(disputeId)


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_message -> {
                startActivity(
                    Intent(this, DisputeChatActivity::class.java).putExtra(
                        AppConstant.ID,
                        disputeId
                    )
                )
            }

        }
    }


    override fun onDisputeDetailsGetSuccessful(response: DisputeDetailsBean) {
        setView(response.data)
    }


    private fun setView(response: DisputeDetailsBean.Data?) {


        txt_job_title.text = response?.jobTitle
        txt_job_description.text = response?.jobDescription
        txt_dispute_raised.text = response?.clientName
        txt_dispute_amount.text = getString(R.string.price_string, response?.milestonePrice)
        txt_dispute_date.text =
            TimeUtils.getServerToAppDate(response?.disputeDateCreated.toString())
        txt_dispute_reason.text = response?.disputeReason

        if (!ValidationUtils.isEmptyFiled(response?.disputeResultFavour.toString())) {
            dispute_reason_layout.visibility = View.VISIBLE
            txt_dispute_result.text = response?.disputeResultFavour.toString()
        } else {
            dispute_reason_layout.visibility = View.GONE
        }

        txt_dispute_status.text = response?.disputeStatus
        if (response?.disputeStatus == DISPUTE_STATUS.resolved.name) {
            txt_dispute_status.background =
                ContextCompat.getDrawable(this, R.drawable.ic_round_active_background)
            txt_dispute_status.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.active_text_color
                )
            )
        } else if (response?.disputeStatus == DISPUTE_STATUS.pending.name) {
            txt_dispute_status.background =
                ContextCompat.getDrawable(this, R.drawable.ic_round_cancelled_background)
            txt_dispute_status.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.cancelled_text_color
                )
            )

        }


    }


    override fun onDisputeDetailsGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null)
            setEmptyView(R.drawable.ic_no_data, loginParams.msg)
    }

    override fun showOnScreenProgress() {
        layout_progress.visibility = View.VISIBLE
    }

    override fun hideOnScreenProgress() {
        layout_progress.visibility = View.GONE
    }

    override fun onApiException(error: Int) {
        setEmptyView(R.drawable.ic_connectivity, getString(error))
    }

    private fun setEmptyView(drawableId: Int, errorMessage: String) {

        no_data_layout.visibility = View.VISIBLE
        txt_no_data_image.setImageResource(drawableId)
        txt_no_data_title.text = errorMessage

    }


}