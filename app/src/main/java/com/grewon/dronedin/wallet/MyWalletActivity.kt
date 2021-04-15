package com.grewon.dronedin.wallet

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.earnings.adapter.EarningsAdapter
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.EarningsDataBean
import com.grewon.dronedin.server.params.WithdrawParams
import com.grewon.dronedin.utils.ValidationUtils
import com.grewon.dronedin.wallet.contract.WalletContract
import kotlinx.android.synthetic.main.activity_my_wallet.*
import kotlinx.android.synthetic.main.layout_no_data.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject

class MyWalletActivity : BaseActivity(), WalletContract.View, View.OnClickListener {

    @Inject
    lateinit var walletPresenter: WalletContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var offsetCount = 1
    private var isLoaded = false
    private var isFirstTimeDataLoaded = false
    private var earningAdapter: EarningsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)

        setClicks()
        initView()
    }

    private fun apiCall() {
        walletPresenter.getEarningsData(offsetCount)
    }


    private fun initView() {

        txt_toolbar_title.text=getString(R.string.my_wallet)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        walletPresenter.attachView(this)
        walletPresenter.attachApiInterface(retrofit)

        apiCall()


        nested_scroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (!isLoaded && isFirstTimeDataLoaded) {
                    isLoaded = true
                    earning_recycle.post(Runnable { earningAdapter?.addLoadingData() })
                    Handler().postDelayed(Runnable {
                        earning_recycle.post(Runnable {
                            apiCall()
                        })
                    }, 200)
                }
            }
        })


    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        txt_withdraw.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_withdraw -> {
                if (ValidationUtils.isEmptyFiled(edt_amount.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_withdraw_amount))
                } else {
                    walletPresenter.withdrawAmount(WithdrawParams(edt_amount.text.toString()))
                }
            }
        }
    }


    override fun onApiException(error: Int) {
        if (offsetCount == 1) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }
    }

    override fun onWalletDataGetSuccessful(response: EarningsDataBean) {
        txt_total_withdraw.text = getString(R.string.price_string, response.totalWithdraw)
        txt_available_balance.text = getString(R.string.price_string, response.walletBalance)
        if (response.data != null) {
            isFirstTimeDataLoaded = true
            isLoaded = true
            if (earningAdapter != null) {
                earningAdapter?.removeLoadingData()
            }
            setEarningData(response.data)
            offsetCount += 1


        } else {

            isLoaded = true
            if (earningAdapter != null) {
                earningAdapter?.removeLoadingData()
            }
            if (offsetCount == 1) {
                if (response.msg != null) {
                    setEmptyView(response.msg, R.drawable.ic_no_data)
                }
            }


        }
    }

    override fun onWalletDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            if (offsetCount == 1) {
                setEmptyView(loginParams.msg, R.drawable.ic_no_data)
            }
        }
    }

    override fun onWithdrawDataSuccessful(response: CommonMessageBean) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)
            offsetCount = 1
            apiCall()
        }
    }

    override fun onWithdrawDataFailed(loginParams: WithdrawParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    private fun setEmptyView(msg: String, drawable: Int) {
        no_data_layout.visibility = View.VISIBLE
        earning_recycle.visibility = View.GONE
        txt_no_data_image.setImageResource(drawable)
        txt_no_data_title.text = msg
    }

    private fun setEarningData(earningList: ArrayList<EarningsDataBean.Data>) {
        isLoaded = false

        earning_recycle.layoutManager = LinearLayoutManager(this)
        if (offsetCount == 1) {
            earningAdapter = EarningsAdapter(this)
            earning_recycle.adapter = earningAdapter
        }
        earningAdapter?.addItemsList(earningList)
        no_data_layout.visibility = View.GONE
        earning_recycle.visibility = View.VISIBLE
    }
}