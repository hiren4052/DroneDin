package com.grewon.dronedin.paymentsummary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.paymentmethod.PaymentMethodActivity
import com.grewon.dronedin.paymentsummary.contract.ActiveMileStoneContract
import com.grewon.dronedin.paymentsummary.presenter.ActiveMileStonePresenter
import com.grewon.dronedin.server.CardDataBean
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.ActiveMilestoneParams
import com.grewon.dronedin.utils.TextUtils
import kotlinx.android.synthetic.main.activity_milestone_summary.*
import kotlinx.android.synthetic.main.activity_milestone_summary.card_type
import kotlinx.android.synthetic.main.activity_milestone_summary.check_wallet
import kotlinx.android.synthetic.main.activity_milestone_summary.txt_app_charge
import kotlinx.android.synthetic.main.activity_milestone_summary.txt_card_number
import kotlinx.android.synthetic.main.activity_milestone_summary.txt_milestone_price
import kotlinx.android.synthetic.main.activity_milestone_summary.txt_name
import kotlinx.android.synthetic.main.activity_milestone_summary.txt_title_droned_in_charge
import kotlinx.android.synthetic.main.activity_milestone_summary.txt_total_amount
import kotlinx.android.synthetic.main.activity_milestone_summary.txt_wallet_deducted
import kotlinx.android.synthetic.main.activity_milestone_summary.wallet_layout
import kotlinx.android.synthetic.main.activity_milestone_summary.wallet_view
import kotlinx.android.synthetic.main.activity_payment_summary.*
import kotlinx.android.synthetic.main.activity_payment_summary.txt_add_edit
import kotlinx.android.synthetic.main.activity_payment_summary.txt_cancel
import kotlinx.android.synthetic.main.activity_payment_summary.txt_pay_now
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject

class MilestoneSummaryActivity : BaseActivity(), View.OnClickListener,
    ActiveMileStoneContract.View {
    private var milestoneId: String = ""
    private var milestonePrice: String = ""
    private var walletPrice: String = "25"
    private var commissionPrice: String = ""

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var activeMileStonePresenter: ActiveMileStoneContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_summary)
        initView()
        setClicks()
    }

    override fun onResume() {
        super.onResume()
        activeMileStonePresenter.getCardData()
    }

    private fun initView() {

        txt_toolbar_title.text = getString(R.string.payment_details)


        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        activeMileStonePresenter.attachView(this)
        activeMileStonePresenter.attachApiInterface(retrofit)

        milestoneId = intent.getStringExtra(AppConstant.ID).toString()
        milestonePrice = intent.getStringExtra(AppConstant.PRICE).toString()

        if (preferenceUtils.getProfileData() != null) {
             walletPrice = preferenceUtils?.getProfileData()!!.data?.userWallet.toString()
            commissionPrice = preferenceUtils?.getProfileData()!!.data?.stripeCommission.toString()
            check_wallet.text = getString(R.string.use_wallet_amount, walletPrice)

            if (walletPrice == "0") {
                check_wallet.visibility = View.GONE
            } else {
                check_wallet.visibility = View.VISIBLE
            }

            txt_title_droned_in_charge.text = getString(R.string.dronedin_charges, commissionPrice)

        }

        txt_milestone_price.text = getString(R.string.price_string, milestonePrice)
        countPrice()

    }

    private fun countPrice() {

        var totalPrice = 0.0
        var commissionAmount = 0.0
        var milestoneAmount = milestonePrice.toDouble()
        var walletAmount = walletPrice.toDouble()
        var commissionPrice = commissionPrice.toDouble()

        commissionAmount = ((milestoneAmount * commissionPrice) / 100)

        txt_app_charge.text =
            getString(R.string.price_string, TextUtils.convertDecimalFormat(commissionAmount))


        if (check_wallet.isChecked) {

            totalPrice = milestoneAmount + commissionAmount

            if (walletAmount < totalPrice) {
                totalPrice = (milestoneAmount + commissionAmount) - walletAmount
                txt_wallet_deducted.text = getString(
                    R.string.minus_price_string,
                    TextUtils.convertDecimalFormat(walletAmount)
                )
            } else {
                txt_wallet_deducted.text = getString(
                    R.string.minus_price_string,
                    TextUtils.convertDecimalFormat(totalPrice)
                )
                totalPrice = 0.0
            }


            wallet_layout.visibility = View.VISIBLE
            wallet_view.visibility = View.VISIBLE

        } else {

            totalPrice = milestoneAmount + commissionAmount
            wallet_layout.visibility = View.GONE
            wallet_view.visibility = View.GONE

        }

        txt_total_amount.text =
            getString(R.string.price_string, TextUtils.convertDecimalFormat(totalPrice))

    }

    private fun setClicks() {
        txt_add_edit.setOnClickListener(this)
        img_back.setOnClickListener(this)
        txt_pay_now.setOnClickListener(this)
        txt_cancel.setOnClickListener(this)
        check_wallet.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.txt_add_edit -> {
                startActivity(Intent(this, PaymentMethodActivity::class.java))
            }

            R.id.img_back -> {
                finish()
            }

            R.id.txt_pay_now -> {
                if (check_wallet.isChecked) {
                   val params=ActiveMilestoneParams(milestoneId,AppConstant.YES_STATUS)
                    activeMileStonePresenter.activeMileStone(params)
                } else {
                    val params=ActiveMilestoneParams(milestoneId,AppConstant.NO_STATUS)
                    activeMileStonePresenter.activeMileStone(params)
                }

            }

            R.id.check_wallet -> {
                countPrice()
            }

            R.id.txt_cancel -> {
                finish()
            }

        }
    }

    override fun onActiveMilestoneSuccessfully(response: CommonMessageBean) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onActiveMilestoneFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onCardDataGetSuccessful(response: CardDataBean) {

        if (response.data?.card != null && response.data?.card.size > 0) {

            card_type.visibility = View.VISIBLE
            txt_card_number.visibility = View.VISIBLE
            txt_name.visibility = View.VISIBLE

            txt_card_number.text = getString(R.string.card_number_string, response.data.defaultCard?.userCardinfoLast4)
            txt_name.text = response.data.defaultCard?.cardHolderName

        } else {
            card_type.visibility = View.GONE
            txt_card_number.visibility = View.GONE
            txt_name.text = getString(R.string.payment_method_not_added_please_add_to_pay_amount)
        }

    }

    override fun onCardDataGetFailed(loginParams: CommonMessageBean) {
        card_type.visibility = View.GONE
        txt_card_number.visibility = View.GONE
        txt_name.text = getString(R.string.payment_method_not_added_please_add_to_pay_amount)
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
        finish()
    }

}