package com.grewon.dronedin.paymentsummary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.paymentmethod.PaymentMethodActivity
import kotlinx.android.synthetic.main.activity_payment_summary.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class PaymentSummaryActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_summary)
        initView()
        setClicks()
    }

    private fun initView() {
        txt_toolbar_title.text=getString(R.string.payment_details)
    }

    private fun setClicks() {
        txt_add_edit.setOnClickListener(this)
        img_back.setOnClickListener(this)
        txt_pay_now.setOnClickListener(this)
        txt_cancel.setOnClickListener(this)
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

            }
            R.id.txt_cancel -> {
                finish()
            }
        }
    }
}