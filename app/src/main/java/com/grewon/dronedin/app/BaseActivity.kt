package com.grewon.dronedin.app


import android.content.Context

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import io.github.inflationx.viewpump.ViewPumpContextWrapper




open class BaseActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }




    override fun attachBaseContext(newBase: Context) {

        super.attachBaseContext(
            ViewPumpContextWrapper.wrap(
                newBase
            )

        )
    }





}