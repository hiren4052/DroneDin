package com.grewon.dronedin.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.utils.TextUtils
import com.grewon.dronedin.verification.VerificationActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_sign_up.setOnClickListener(this)
        txt_sign_in.setOnClickListener(this)
    }

    private fun initView() {
        txt_terms.text = TextUtils.appTermsSpannableString(
            this,
            getString(R.string.by_logging_in_you_agree_with_the_terms_and_conditions_learn_more)
        )
        txt_terms.movementMethod = LinkMovementMethod.getInstance()
        txt_sign_in.text = TextUtils.signInColorSpannableString(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_sign_up -> {
                startActivity(Intent(this, VerificationActivity::class.java))
            }
            R.id.txt_sign_in -> {
                finish()
            }
        }
    }
}