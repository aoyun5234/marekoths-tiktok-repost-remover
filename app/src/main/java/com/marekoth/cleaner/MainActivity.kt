package com.marekoth.cleaner

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.JavascriptInterface
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var txtCleaned: TextView
    private lateinit var txtScanned: TextView
    private lateinit var txtStatus: TextView
    private var isRunning = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        txtCleaned = findViewById(R.id.txtCleaned)
        txtScanned = findViewById(R.id.txtScanned)
        txtStatus = findViewById(R.id.txtStatus)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnStop = findViewById<Button>(R.id.btnStop)

        // WebView ayarları
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // Sayfa yüklenince temizleyiciyi hazır et
                injectCleaner()
            }
        }

        // JS -> Kotlin köprüsü (sayaç için)
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun onStats(cleaned: Int, scanned: Int, status: String) {
                runOnUiThread {
                    txtCleaned.text = "$cleaned CLEANED"
                    txtScanned.text = "$scanned SCANNED"
                    txtStatus.text = status
                }
            }
        }, "AndroidBridge")

        // Başlangıçta TikTok profiline git (kullanıcı login olacak)
        webView.loadUrl("https://www.tiktok.com/login")

        btnStart.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                txtStatus.text = "Running..."
                // Reklamı gizle - rahatsız etmesin
                findViewById<TextView>(R.id.adPlaceholder).visibility = android.view.View.GONE
                webView.evaluateJavascript("window.startMarekothCleaner()", null)
                Toast.makeText(this, "Temizlik başladı! Reposts sekmesine gidin.", Toast.LENGTH_LONG).show()
            }
        }

        btnStop.setOnClickListener {
            isRunning = false
            txtStatus.text = "Stopped"
            findViewById<TextView>(R.id.adPlaceholder).visibility = android.view.View.VISIBLE
            webView.evaluateJavascript("window.stopMarekothCleaner()", null)
        }
    }

    private fun injectCleaner() {
        try {
            val js = assets.open("cleaner.js").bufferedReader().use { it.readText() }
            webView.evaluateJavascript(js, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}