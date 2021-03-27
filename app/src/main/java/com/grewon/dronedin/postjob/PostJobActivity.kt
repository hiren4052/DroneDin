package com.grewon.dronedin.postjob

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.server.params.CreateJobsParams
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class PostJobActivity : BaseActivity(), View.OnClickListener {

    var createJobsParams: CreateJobsParams? = null
    var screenTag: String = ""
    var jobId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job)
        initView()
        setClicks()
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        screenTag = intent.getStringExtra(AppConstant.TAG).toString()
        jobId = intent.getStringExtra(AppConstant.ID).toString()
        if (screenTag == "edit") {
            txt_toolbar_title.text = getString(R.string.edit_job)
            createJobsParams = intent.getParcelableExtra(AppConstant.BEAN)
        } else {
            txt_toolbar_title.text = getString(R.string.post_new_job)
            createJobsParams = CreateJobsParams()

        }

        loadFragment(SelectFragmentFragment())

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_contain, fragment)
            .commit()
    }

    fun showFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_contain, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                onBackPressed()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_contain)
        fragment!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}