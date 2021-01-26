package com.grewon.dronedin.web


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant

import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.helper.LogX
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : BaseActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
       // supportActionBar?.title = intent.getStringExtra(AppConstant.TAG)


        webview.webViewClient = MyyWebClient()
        webview.settings.javaScriptEnabled = true
        webview.settings.setSupportZoom(true)
        webview.settings.builtInZoomControls = true
        if (intent.extras != null && intent.getStringExtra(
                AppConstant.WEB_URL
            ) != null
        ) {
            webview.loadUrl(intent.getStringExtra(AppConstant.WEB_URL)!!)
            LogX.E(
                "" + intent.getStringExtra(
                    AppConstant.WEB_URL
                )
            )
        }
    }

    inner class MyyWebClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progress.visibility = View.VISIBLE
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true

        }


        override fun onPageFinished(view: WebView, url: String) {
            progress.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

}
