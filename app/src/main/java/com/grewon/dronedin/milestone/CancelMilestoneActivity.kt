package com.grewon.dronedin.milestone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import kotlinx.android.synthetic.main.activity_cancel_milestone.*

class CancelMilestoneActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_milestone)
        initView()
    }

    private fun initView() {
        if (isPilotAccount()) {
            text_about_info.text = getString(R.string.cancel_milestone_pilot_description)
        } else {
            text_about_info.text = getString(R.string.cancel_milestone_client_description)
        }
    }
}