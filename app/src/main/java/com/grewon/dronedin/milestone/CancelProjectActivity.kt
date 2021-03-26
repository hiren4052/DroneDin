package com.grewon.dronedin.milestone

import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import kotlinx.android.synthetic.main.activity_cancel_project.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class CancelProjectActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_project)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_send_request.setOnClickListener(this)
        txt_cancel_forcefully.setOnClickListener(this)
        txt_cancel_project.setOnClickListener(this)
    }

    private fun initView() {
        txt_toolbar_title.text = getString(R.string.cancel_project)
        if (isPilotAccount()) {
            layout_pilot.visibility = View.VISIBLE
            txt_cancel_project.visibility = View.GONE
            text_about_info.text = getString(R.string.cancel_project_pilot_description)
        } else {
            layout_pilot.visibility = View.GONE
            txt_cancel_project.visibility = View.VISIBLE
            text_about_info.text = getString(R.string.cancel_project_client_description)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_send_request -> {

            }
            R.id.txt_cancel_forcefully -> {

            }
            R.id.txt_cancel_project -> {

            }
        }
    }
}