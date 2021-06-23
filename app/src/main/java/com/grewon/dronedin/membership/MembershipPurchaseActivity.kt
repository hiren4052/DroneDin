package com.grewon.dronedin.membership

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.membership.contract.MembershipPurchaseContract
import com.grewon.dronedin.membership.presenter.MembershipPurchasePresenter
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.MemberShipBean
import com.grewon.dronedin.server.params.AddCardParams
import com.grewon.dronedin.server.params.PurchasePackageParams
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_membership_purchase.*
import retrofit2.Retrofit
import javax.inject.Inject


class MembershipPurchaseActivity : BaseActivity(), View.OnClickListener,
    MembershipPurchaseContract.View {

    private var membershipBean: MemberShipBean.Data? = null

    @Inject
    lateinit var membershipPurchasePresenter: MembershipPurchaseContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membership_purchase)
        DroneDinApp.getAppInstance().loadGifImage(R.drawable.credit_card_animation, top_image)

        initView()
        setClicks()

    }

    private fun setClicks() {
        im_back.setOnClickListener(this)
        txt_purchase.setOnClickListener(this)
        txt_year.setOnClickListener(this)
        txt_month.setOnClickListener(this)
    }


    private fun initView() {

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        membershipPurchasePresenter.attachView(this)
        membershipPurchasePresenter.attachApiInterface(retrofit)


        membershipBean = intent.getParcelableExtra(AppConstant.BEAN)

        txt_package_name.text = getString(
            R.string.package_name_price,
            membershipBean?.packageName,
            membershipBean?.packagePrice
        )
        txt_package_description.text = membershipBean?.packageDescription


        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            ListUtils.getMonthList()
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        month_spinner.adapter = adapter

        month_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                txt_month.text = ListUtils.getMonthList()[position]

            }

        }


        val yearAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            ListUtils.getYearList()
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        year_spinner.adapter = yearAdapter

        year_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                txt_year.text = ListUtils.getYearList()[position]

            }

        }


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_purchase -> {
                when {
                    ValidationUtils.isEmptyFiled(edt_name.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_card_holder_name))
                    }
                    ValidationUtils.isEmptyFiled(edt_card_number.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_card_number))
                    }
                    ValidationUtils.isEmptyFiled(edt_cvv.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_card_cvv_number))
                    }
                    else -> {
                        DroneDinApp.loadingDialogMessage = getString(R.string.purchasing)
                        val purchaseParams = PurchasePackageParams(
                            edt_name.text.toString(),
                            edt_card_number.creditCardNumber,
                            edt_cvv.text.toString(),
                            txt_month.text.toString(),
                            txt_year.text.toString(),
                            membershipBean?.packageId
                        )
                        membershipPurchasePresenter.purchaseMemberShip(purchaseParams)
                    }
                }
            }
            R.id.im_back -> {
                finish()
            }
            R.id.txt_month -> {
                month_spinner.performClick()
            }
            R.id.txt_year -> {
                year_spinner.performClick()
            }
        }
    }

    override fun onMembershipPurchaseSuccessful(response: CommonMessageBean) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)
        }
    }

    override fun onMembershipPurchaseFailed(loginParams: PurchasePackageParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

}