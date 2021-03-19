package com.grewon.dronedin.postjob

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.server.params.CreateJobsParams
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class PostJobActivity : BaseActivity(), View.OnClickListener {

    var createJobsParams: CreateJobsParams? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job)
        initView()
        setClicks()
        loadFragment(SelectFragmentFragment())
    }

    private fun setClicks() {
        img_back.setOnClickListener(this)
    }

    private fun initView() {
        createJobsParams = CreateJobsParams()
        txt_toolbar_title.text = getString(R.string.post_new_job)
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