package com.example.healthgates.ui.activity

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.healthgates.R
import java.util.ResourceBundle.getBundle

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        webView = findViewById(R.id.webview)
//        webView.settings.setJavaScriptEnabled(true)

        val url = intent.getStringExtra("url")

        webView.loadUrl(url!!)

        WebView.setWebContentsDebuggingEnabled(false)
    }
}