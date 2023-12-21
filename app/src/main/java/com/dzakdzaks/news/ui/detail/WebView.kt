package com.dzakdzaks.news.ui.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(
    modifier: Modifier = Modifier,
    url: String,
    onSetWebView: (WebView) -> Unit,
    onSetWebViewCanGoBack: (Boolean) -> Unit,
    onProgressChanged: (Int) -> Unit,
    onPageFinished: (Boolean) -> Unit,
) {

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        onProgressChanged(newProgress)
                    }
                }
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                        onPageFinished(false)
                        onSetWebViewCanGoBack(view.canGoBack())
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        onPageFinished(true)
                    }
                }
                settings.javaScriptEnabled = true

                loadUrl(url)
                onSetWebView(this)
            }
        },
        update = {
            onSetWebView(it)
        }
    )
}
