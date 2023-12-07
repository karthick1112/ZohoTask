package com.example.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ViewMoreWebView : AppCompatActivity() {

   lateinit var viewMoreWB : WebView
   lateinit var bck : LinearLayout

    var url = ""
    var idPBLoading: ProgressBar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_more)
        idPBLoading = findViewById(R.id.idPBLoading)
        idPBLoading!!.visibility = View.VISIBLE
        val extras = intent.extras
        if (extras != null) {
            url = extras.getString("url").toString()
            Log.e("url", url)
        }

        viewMoreWB = findViewById(R.id.viewMoreWB)
        bck = findViewById(R.id.bck)
        webViewSetup(url)

        bck.setOnClickListener {
            onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup(urll: String) {
        viewMoreWB.webViewClient = WebViewClient()
        idPBLoading!!.visibility = View.VISIBLE

        viewMoreWB.apply {
            loadUrl(urll.toString())
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
            settings.domStorageEnabled = true
            val settings: WebSettings = viewMoreWB.getSettings()
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.javaScriptEnabled = true
//            viewMoreWB.setWebViewClient(object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                    view.loadUrl(url)
//                    return true
//                }
//
//                override fun onPageFinished(view: WebView, url: String) {
//                    idPBLoading!!.visibility = View.GONE
//
//                }
//
//                override fun onReceivedError(
//                    view: WebView,
//                    errorCode: Int,
//                    description: String,
//                    failingUrl: String
//                ) {
//                    Toast.makeText(this@ViewMoreWebView, "Error:$description", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            })
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.databaseEnabled = false
            settings.domStorageEnabled = false
            settings.setGeolocationEnabled(false)
            settings.saveFormData = false
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
            idPBLoading!!.visibility = View.GONE
        }
    }

}