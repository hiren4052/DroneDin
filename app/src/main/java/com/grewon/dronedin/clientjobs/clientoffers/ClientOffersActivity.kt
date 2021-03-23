package com.grewon.dronedin.clientjobs.clientoffers

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.clientjobs.adapter.ClientOffersAdapter
import com.grewon.dronedin.server.OffersDataBean
import kotlinx.android.synthetic.main.activity_client_offers.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class ClientOffersActivity : BaseActivity(), View.OnClickListener,
    ClientOffersAdapter.OnItemClickListeners {

    private var clientOffersAdapter: ClientOffersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_offers)
        setClicks()
        initView()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        txt_toolbar_title.text = getString(R.string.offers)


        offers_recycle.layoutManager = LinearLayoutManager(this)
        clientOffersAdapter = ClientOffersAdapter(this, this)
        offers_recycle.adapter = clientOffersAdapter
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }


    override fun onOffersItemClick(jobsDataBean: OffersDataBean.Data?) {
        startActivity(Intent(this, ClientOffersDetailsActivity::class.java))
    }
}