package com.grewon.dronedin.milestone

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.milestone.adapter.CreateMileStoneAdapter
import com.grewon.dronedin.server.CreateMilestoneBean
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_cancel_milestone.*
import kotlinx.android.synthetic.main.activity_milestone_add.*
import kotlinx.android.synthetic.main.activity_milestone_add.text_about_info
import kotlinx.android.synthetic.main.activity_milestone_add.txt_send_request
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class MilestoneAddActivity : BaseActivity(), View.OnClickListener {

    private var createMileStoneAdapter: CreateMileStoneAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_milestone_add)

        setClicks()
        initView()
    }

    private fun setClicks() {
        txt_send_request.setOnClickListener(this)
        im_add_milestone.setOnClickListener(this)
        img_back.setOnClickListener(this)
    }

    private fun initView() {

        txt_toolbar_title.text = getString(R.string.add_milestones)

        create_milestone_recycle.layoutManager = LinearLayoutManager(this)
        createMileStoneAdapter = CreateMileStoneAdapter(this)
        create_milestone_recycle.adapter = createMileStoneAdapter


        if (isPilotAccount()) {
            txt_send_request.text = getString(R.string.send_request)
            text_about_info.text = getString(R.string.milestone_add_pilot_description)
        } else {
            txt_send_request.text = getString(R.string.add_milestones)
            text_about_info.text = getString(R.string.milestone_add_client_description)
        }



    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.im_add_milestone -> {
                when {
                    ValidationUtils.isEmptyFiled(edt_milestone_price.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_milestone_price))
                    }
                    ValidationUtils.isEmptyFiled(edt_milestone_description.text.toString()) -> {
                        DroneDinApp.getAppInstance()
                            .showToast(getString(R.string.please_enter_milestone_description))
                    }
                    else -> {


                        createMileStoneAdapter?.addItems(
                            CreateMilestoneBean(
                                edt_milestone_description.text.toString(),
                                edt_milestone_price.text.toString()
                            )
                        )

                        edt_milestone_price.setText("")
                        edt_milestone_description.setText("")

                    }
                }
            }
            R.id.img_back -> {
                finish()
            }
        }

    }

}