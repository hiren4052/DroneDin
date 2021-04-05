package com.grewon.dronedin.paymentmethod

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.addbank.AddBankAccountActivity
import com.grewon.dronedin.addcard.AddCardActivity
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.dialogs.AlertViewDialog
import com.grewon.dronedin.paymentmethod.adapter.BankDataAdapter
import com.grewon.dronedin.paymentmethod.adapter.CredtiCardDataAdapter
import com.grewon.dronedin.paymentmethod.contract.PaymentMethodContract
import com.grewon.dronedin.server.BankDataBean
import com.grewon.dronedin.server.CardDataBean
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.params.DefaultCardParams
import kotlinx.android.synthetic.main.activity_payment_method.*
import kotlinx.android.synthetic.main.activity_payment_method.no_data_layout
import kotlinx.android.synthetic.main.layout_no_data.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject
import kotlin.collections.ArrayList

class PaymentMethodActivity : BaseActivity(), BankDataAdapter.OnItemCLickListeners,
    View.OnClickListener, CredtiCardDataAdapter.OnCardItemCLickListeners,
    PaymentMethodContract.View {


    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var paymentMethodPresenter: PaymentMethodContract.Presenter

    private var bankDataAdapter: BankDataAdapter? = null
    private var cardDataAdapter: CredtiCardDataAdapter? = null
    private var cardSelectId: String = ""
    private var bankSelectId: String = ""
    private var alertDialog: AlertViewDialog? = null
    private var adapterPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
        setClicks()
        initView()
    }

    private fun initView() {

        txt_toolbar_title.text = getText(R.string.payment_method)


        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        paymentMethodPresenter.attachView(this)
        paymentMethodPresenter.attachApiInterface(retrofit)


        apiCall()

    }

    private fun apiCall() {
        if (isPilotAccount()) {
            txt_name.text = getString(R.string.account_details)
            paymentMethodPresenter.getBankData()
        } else {
            txt_name.text = getString(R.string.card_details)
            paymentMethodPresenter.getCardData()
        }
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        fab_add_bank.setOnClickListener(this)

    }


    override fun onDeleteItem(itemView: BankDataBean.Data.Bank?, adapterPosition: Int) {
        openDeleteAlertDialog(itemView?.id.toString(), adapterPosition)
    }

    private fun openDeleteAlertDialog(deleteID: String, adapterPosition: Int) {
        this.adapterPosition = adapterPosition
        alertDialog = AlertViewDialog(this, R.style.DialogThme)
        alertDialog!!.setTitle(getString(R.string.delete_message))
        alertDialog!!.setPositiveBtnTxt(getString(R.string.yes))
        alertDialog!!.setNegativeBtnTxt(getString(R.string.no))
        alertDialog!!.setOkListener(View.OnClickListener {
            if (isPilotAccount()) {
                paymentMethodPresenter.deleteBankData(deleteID)
            } else {
                paymentMethodPresenter.deleteCardData(deleteID)
            }

        })
        alertDialog!!.show()
    }


    override fun onDefaultSelect(itemView: BankDataBean.Data.Bank?) {
        bankSelectId = itemView?.id.toString()
        paymentMethodPresenter.setDefaultBankData(DefaultCardParams(itemView?.id))
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> {
                finish()
            }
            R.id.fab_add_bank -> {
                if (isPilotAccount()) {
                    startActivityForResult(Intent(this, AddBankAccountActivity::class.java), 12)
                } else {
                    startActivityForResult(Intent(this, AddCardActivity::class.java), 12)
                }
            }
        }
    }


    override fun onDeleteCardItem(itemView: CardDataBean.Data.Card, adapterPosition: Int) {
        openDeleteAlertDialog(itemView.id.toString(), adapterPosition)
    }

    override fun onDefaultCardSelect(itemView: CardDataBean.Data.Card) {
        cardSelectId = itemView.id.toString()
        paymentMethodPresenter.setDefaultCardData(DefaultCardParams(itemView.id))
    }

    override fun onApiException(error: Int) {
        setEmptyView(R.drawable.ic_connectivity, getString(error))
    }

    override fun onCardDataGetSuccessful(response: CardDataBean) {
        if (response.data?.card != null && response.data?.card.size > 0) {
            setCardAdapter(response.data.card, response.data.defaultCard?.userCardinfoStripeSource)
        } else {
            if (response.msg != null) {
                setEmptyView(R.drawable.ic_no_data, response.msg)
            }
        }
    }

    private fun setCardAdapter(
        card: ArrayList<CardDataBean.Data.Card>,
        userCardinfoStripeSource: String?
    ) {
        no_data_layout.visibility = View.GONE
        payment_method_recycle.visibility = View.VISIBLE
        payment_method_recycle.layoutManager = LinearLayoutManager(this)
        cardDataAdapter = CredtiCardDataAdapter(this, userCardinfoStripeSource.toString(), this)
        payment_method_recycle.adapter = cardDataAdapter
        cardDataAdapter?.addItemsList(card)
    }

    private fun setBankAdapter(
        bank: ArrayList<BankDataBean.Data.Bank>,
        userBankAccountBankId: String?
    ) {
        no_data_layout.visibility = View.GONE
        payment_method_recycle.visibility = View.VISIBLE
        payment_method_recycle.layoutManager = LinearLayoutManager(this)
        bankDataAdapter = BankDataAdapter(this, userBankAccountBankId.toString(), this)
        payment_method_recycle.adapter = bankDataAdapter
        bankDataAdapter?.addItemsList(bank)
    }

    override fun onCardDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            setEmptyView(R.drawable.ic_no_data, loginParams.msg)
        }
    }

    override fun onCardSelectSuccessfully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            if (cardSelectId != null) {
                cardDataAdapter?.setDefaultId(cardSelectId)
            }
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onCardSelectFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onCardDeleteSuccessfully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            if (alertDialog != null) {
                alertDialog?.dismiss()
            }
            cardDataAdapter?.removeCardData(adapterPosition)
        }
    }

    override fun onCardDeleteFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onBankDataGetSuccessful(response: BankDataBean) {
        if (response.data?.bank != null && response.data?.bank.size > 0) {
            setBankAdapter(
                response.data?.bank,
                response.data.defaultBank?.userBankAccountBankId
            )
        } else {
            if (response.msg != null) {
                setEmptyView(R.drawable.ic_no_data, response.msg)
            }
        }
    }

    override fun onBankDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            setEmptyView(R.drawable.ic_no_data, loginParams.msg)
        }
    }

    override fun onBankSelectSuccessfully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            if (bankSelectId != null) {
                bankDataAdapter?.setDefaultId(bankSelectId)
            }
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onBankSelectFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onBankDeleteSuccessfully(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            bankDataAdapter?.removeBankData(adapterPosition)
            if (alertDialog != null) {
                alertDialog?.dismiss()
            }
        }
    }

    override fun onBankDeleteFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onShowScreenProgress() {
        layout_progress.visibility = View.VISIBLE
    }

    override fun onHideScreenProgress() {
        layout_progress.visibility = View.GONE
    }

    private fun setEmptyView(drawableId: Int, errorMessage: String) {
        no_data_layout.visibility = View.VISIBLE
        txt_no_data_image.setImageResource(drawableId)
        txt_no_data_title.text = errorMessage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == RESULT_OK) {
            apiCall()
        }
    }


}