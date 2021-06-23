package com.grewon.dronedin.addcard

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.addcard.contract.AddCardContract
import com.grewon.dronedin.server.CardDataBean
import com.grewon.dronedin.server.params.AddCardParams
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_add_card.*
import retrofit2.Retrofit
import javax.inject.Inject


class AddCardActivity : BaseActivity(), View.OnClickListener, AddCardContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var addCardPresenter: AddCardContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_save.setOnClickListener(this)
        im_back.setOnClickListener(this)
        txt_year.setOnClickListener(this)
        txt_month.setOnClickListener(this)
    }

    private fun initView() {

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        addCardPresenter.attachView(this)
        addCardPresenter.attachApiInterface(retrofit)


        DroneDinApp.getAppInstance().loadGifImage(R.drawable.credit_card_animation, top_image)

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
            R.id.txt_save -> {
                if (ValidationUtils.isEmptyFiled(edt_name.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_card_holder_name))
                } else if (ValidationUtils.isEmptyFiled(edt_card_number.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_card_number))
                } else if (ValidationUtils.isEmptyFiled(edt_cvv.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_card_cvv_number))
                } else {

                    DroneDinApp.loadingDialogMessage = getString(R.string.adding)

                    val addCardParams = AddCardParams(
                        edt_name.text.toString(),
                        edt_card_number.creditCardNumber,
                        edt_cvv.text.toString(),
                        txt_month.text.toString(),
                        txt_year.text.toString(),

                        )
                    addCardPresenter.createCard(addCardParams)
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

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onCreateCardSuccessful(response: CardDataBean) {
        if (response.msg != null) {
            DroneDinApp.getAppInstance().showToast(response.msg)
            setResult(RESULT_OK)
            finish()
        }

    }

    override fun onCreateCardFailed(loginParams: AddCardParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
    }

}