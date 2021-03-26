package com.grewon.dronedin.milestone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import kotlinx.android.synthetic.main.activity_end_project.*

class EndProjectActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_project)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_end_project.setOnClickListener(this)
        txt_end_forcefully.setOnClickListener(this)
    }

    private fun initView() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_end_project -> {

            }
            R.id.txt_end_forcefully -> {

            }
        }
    }
}