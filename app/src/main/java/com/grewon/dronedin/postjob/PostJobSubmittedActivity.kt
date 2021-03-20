package com.grewon.dronedin.postjob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.invitepilot.InvitePilotActivity
import kotlinx.android.synthetic.main.activity_post_job_submitted.*

class PostJobSubmittedActivity : BaseActivity(), View.OnClickListener {
    private var jobId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job_submitted)
        setCliCks()
        initView()
    }

    private fun initView() {
        jobId = intent.getStringExtra(AppConstant.ID)!!
    }

    private fun setCliCks() {
        txt_invite.setOnClickListener(this)
        txt_skip.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_invite -> {
                startActivityForResult(
                    Intent(this, InvitePilotActivity::class.java).putExtra(
                        AppConstant.ID,
                        jobId
                    ), 44
                )
            }
            R.id.txt_skip -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 44 && resultCode == RESULT_OK) {
            onBackPressed()
        }
    }
}