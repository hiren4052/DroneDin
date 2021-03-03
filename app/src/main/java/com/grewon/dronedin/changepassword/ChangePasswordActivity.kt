package com.grewon.dronedin.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        setClicks()
    }

    private fun setClicks() {

        im_back.setOnClickListener(this)
        txt_save.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.txt_save -> {
                finish()
            }
        }
    }
}