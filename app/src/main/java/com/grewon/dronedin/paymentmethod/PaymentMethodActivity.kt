package com.grewon.dronedin.paymentmethod

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.addbank.AddBankAccountActivity
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.paymentmethod.adapter.BankDataAdapter
import com.grewon.dronedin.server.BankDataBean
import kotlinx.android.synthetic.main.activity_add_bank_account.*
import kotlinx.android.synthetic.main.activity_payment_method.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class PaymentMethodActivity : BaseActivity(), BankDataAdapter.OnItemCLickListeners,
    View.OnClickListener {

    private var bankDataAdapter: BankDataAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
        setClicks()
        initView()
    }

    private fun initView() {
        payment_method_recycle.layoutManager = LinearLayoutManager(this)
        bankDataAdapter = BankDataAdapter(this, "", this)
        payment_method_recycle.adapter = bankDataAdapter
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
        fab_add_bank.setOnClickListener(this)

    }

    override fun onDeleteItem(itemView: BankDataBean.Result.Data?, adapterPosition: Int) {

    }

    override fun onDefaultSelect(itemView: BankDataBean.Result.Data?) {
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> {
                finish()
            }
            R.id.fab_add_bank -> {
                startActivity(Intent(this, AddBankAccountActivity::class.java))
            }
        }
    }
}