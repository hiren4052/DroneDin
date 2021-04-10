package com.grewon.dronedin.membership

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.membership.adapter.MembershipAdapter
import com.grewon.dronedin.membership.contract.MembershipListContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.MemberShipBean
import kotlinx.android.synthetic.main.activity_member_ship.*
import kotlinx.android.synthetic.main.layout_no_data.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import java.util.ArrayList
import javax.inject.Inject

class MemberShipActivity : BaseActivity(), MembershipListContract.View, View.OnClickListener,
    MembershipAdapter.OnItemClickListeners {

    @Inject
    lateinit var membershipPresenter: MembershipListContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var membershipAdapter: MembershipAdapter? = null
    private var tag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_ship)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_skip.setOnClickListener(this)
        img_back.setOnClickListener(this)
    }


    private fun initView() {
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        membershipPresenter.attachView(this)
        membershipPresenter.attachApiInterface(retrofit)
        membershipPresenter.getMembershipList()

        if (intent.getBooleanExtra(AppConstant.TAG, false)) {
            toolbar_layout.visibility = View.GONE
            top_layout.visibility = View.VISIBLE
        } else {
            toolbar_layout.visibility = View.VISIBLE
            top_layout.visibility = View.GONE
            txt_toolbar_title.text = getString(R.string.packages)
        }
    }

    override fun onMembershipListSuccessful(response: MemberShipBean) {
        if (response.data != null && response.data.size > 0) {
            setAdapter(response.data)
        } else {
            if (response.msg != null)
                setEmptyView(R.drawable.ic_no_data, response.msg)
        }
    }

    private fun setAdapter(data: ArrayList<MemberShipBean.Data>) {
        no_data_layout.visibility = View.GONE
        membership_package.layoutManager = GridLayoutManager(this, 2)
        membershipAdapter = MembershipAdapter(this, this)
        membership_package.adapter = membershipAdapter
        membershipAdapter?.addItemsList(data)
    }

    override fun onMembershipListFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null)
            setEmptyView(R.drawable.ic_no_data, loginParams.msg)
    }

    override fun onApiException(error: Int) {
        setEmptyView(R.drawable.ic_connectivity, getString(error))
    }

    override fun showOnScreenProgress() {
        layout_progress.visibility = View.VISIBLE
    }

    override fun hideOnScreenProgress() {
        layout_progress.visibility = View.GONE
    }

    private fun setEmptyView(drawableId: Int, errorMessage: String) {
        no_data_layout.visibility = View.VISIBLE
        txt_no_data_image.setImageResource(drawableId)
        txt_no_data_title.text = errorMessage
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_skip -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.img_back -> {
                finish()
            }
        }
    }

    override fun onMembershipItemClick(jobsDataBean: MemberShipBean.Data?) {
        startActivity(
            Intent(
                this,
                MembershipPurchaseActivity::class.java
            ).putExtra(AppConstant.BEAN, jobsDataBean)
        )
    }

}