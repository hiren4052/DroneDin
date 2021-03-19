package com.grewon.dronedin.verification

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.addprofile.AddProfileActivity
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.utils.ScreenUtils
import com.grewon.dronedin.utils.TextUtils
import kotlinx.android.synthetic.main.activity_verification.*


class VerificationActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtils.changeStatusBarColor(this, Color.WHITE)
        setContentView(R.layout.activity_verification)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_submit.setOnClickListener(this)
        im_back.setOnClickListener(this)
    }

    private fun initView() {
        DroneDinApp.getAppInstance().loadGifImage(R.drawable.verifcation_image, top_image)
        txt_receive_code.text = TextUtils.receiveCodeColorSpannableString(this)
        txt_number.text = preferenceUtils.getLoginCredentials()?.data?.userEmail.toString()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_submit -> {
                startActivity(Intent(this, AddProfileActivity::class.java))
            }
            R.id.txt_receive_code -> {

            }
            R.id.im_back -> {
                finish()
            }
        }
    }


}