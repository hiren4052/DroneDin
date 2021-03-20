package com.grewon.dronedin.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.intro.IntroActivity
import com.grewon.dronedin.main.MainActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            if (preferenceUtils.getLoginCredentials() != null && preferenceUtils.getLoginCredentials()?.data != null && preferenceUtils.getLoginCredentials()?.data!!.isStepComplete!!) {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
                finishAffinity()
            }

        }, 1000)
    }
}