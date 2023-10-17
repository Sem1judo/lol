package com.oceanluc.kywheels.activities


import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.oceanluc.kywheels.R
import com.oceanluc.kywheels.tools.PreferencesHelper

class WebRemoveViewActivity : AppCompatActivity() {
    private lateinit var userPrefs: SavedUserPrefs


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_privacy_view)

        val url = intent.getStringExtra("url")
        val webView: WebView = findViewById(R.id.webView)
        val acceptButton: TextView = findViewById(R.id.accept_btn)

        webView.settings.javaScriptEnabled = true

        userPrefs = SavedUserPrefs(this)
        val preferencesHelper = PreferencesHelper(this)

        webView.webViewClient =
            object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null) {
                        view?.loadUrl(url)
                    }
                    return true
                }
            }

        if (url != null) {
            webView.loadUrl(url)
        }
        acceptButton.setOnClickListener {
            startActivity(intent)
            preferencesHelper.balance = 15000
            preferencesHelper.isReg = false
            startActivity(Intent(this, SplashScreen::class.java))
        }
    }

    private fun switchToFragment(fr: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fr).commit()
    }
}
