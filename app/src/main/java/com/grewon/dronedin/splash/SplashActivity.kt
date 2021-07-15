package com.grewon.dronedin.splash

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.intro.IntroActivity
import com.grewon.dronedin.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val mediaController = android.widget.MediaController(this)
        mediaController.setAnchorView(video_view)
        video_view.setMediaController(mediaController)
        mediaController.visibility = View.GONE

        video_view.setVideoURI(
            Uri.parse(
                "android.resource://" + packageName + "/" +
                        R.raw.splash_video
            )
        )
        video_view.start()
        video_view.setOnPreparedListener { it.setVolume(0f, 0f) }
        video_view.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            if (preferenceUtils.getLoginCredentials() != null && preferenceUtils.getLoginCredentials()?.data != null && preferenceUtils.getLoginCredentials()?.data!!.isStepComplete!!) {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
                finishAffinity()
            }
        })

    }
}