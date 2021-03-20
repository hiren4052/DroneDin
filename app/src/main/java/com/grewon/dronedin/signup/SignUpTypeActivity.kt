package com.grewon.dronedin.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import kotlinx.android.synthetic.main.activity_sign_up_type.*

class SignUpTypeActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_type)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_pilot.setOnClickListener(this)
        txt_client.setOnClickListener(this)
    }

    private fun initView() {
        DroneDinApp.getAppInstance().loadGifImage(R.drawable.signup_pilot,pilot_gif)
        DroneDinApp.getAppInstance().loadGifImage(R.drawable.signup_client,client_gif)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_pilot -> {
                startActivity(
                    Intent(
                        this,
                        SignUpActivity::class.java
                    ).putExtra(AppConstant.USER_TYPE, AppConstant.USER_PILOT)
                )
            }
            R.id.txt_client -> {
                startActivity(
                    Intent(
                        this,
                        SignUpActivity::class.java
                    ).putExtra(AppConstant.USER_TYPE, AppConstant.USER_CLIENT)
                )
            }


        }
    }
}