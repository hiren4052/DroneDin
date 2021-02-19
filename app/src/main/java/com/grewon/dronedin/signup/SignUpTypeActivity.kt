package com.grewon.dronedin.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
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
        Glide.with(this).asGif().load(R.drawable.signup_pilot).into(pilot_gif)
        Glide.with(this).asGif().load(R.drawable.signup_client).into(client_gif)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_pilot -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            R.id.txt_client -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
        }
    }
}