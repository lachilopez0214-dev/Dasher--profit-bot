package com.dasherprofitbot.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse

class MainActivity : ComponentActivity() {
 private lateinit var webView: WebView
 private var pendingOrigin: String? = null
 private var pendingCallback: GeolocationPermissions.Callback? = null
 private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
  val allowed = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
  pendingCallback?.invoke(pendingOrigin, allowed, false)
  pendingOrigin = null
  pendingCallback = null
 }
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  webView = WebView(this)
  setContentView(webView)
  val loader = WebViewAssetLoader.Builder().addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this)).build()
  webView.settings.javaScriptEnabled = true
  webView.settings.domStorageEnabled = true
  webView.settings.setGeolocationEnabled(true)
  webView.webViewClient = object : WebViewClientCompat() {
   override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? = loader.shouldInterceptRequest(request.url)
   override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean = request.url.host != "appassets.androidplatform.net"
  }
  webView.webChromeClient = object : WebChromeClient() {
   override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
    if (origin != "https://appassets.androidplatform.net") { callback.invoke(origin, false, false); return }
    val allowed = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    if (allowed) callback.invoke(origin, true, false) else {
     pendingOrigin = origin
     pendingCallback = callback
     permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }
   }
  }
  webView.loadUrl("https://appassets.androidplatform.net/assets/index.html")
 }
 override fun onDestroy() { webView.destroy(); super.onDestroy() }
}