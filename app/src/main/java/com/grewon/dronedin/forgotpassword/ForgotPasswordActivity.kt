package com.grewon.dronedin.forgotpassword

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.utils.ScreenUtils
import com.grewon.dronedin.utils.TextUtils
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.im_back
import kotlinx.android.synthetic.main.activity_forgot_password.txt_receive_code

class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtils.changeStatusBarColor(this, Color.WHITE)
        setContentView(R.layout.activity_forgot_password)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_send.setOnClickListener(this)
        im_back.setOnClickListener(this)

    }

    private fun initView() {
        txt_receive_code.text = TextUtils.receiveCodeColorSpannableString(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_send -> {
                finish()
            }
            R.id.im_back -> {
                finish()
            }
        }
    }
}