package com.zerobudget.browser.browser

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class BrowserWebViewClient(
    private val onState: (PageLoadState) -> Unit,
    private val onUrl: (String) -> Unit
) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString() ?: return false
        return false.also { if (url.isNotBlank()) onUrl(url) }
    }
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        if (!url.isNullOrBlank()) onUrl(url)
        onState(PageLoadState.Loading(0))
    }
    override fun onPageFinished(view: WebView?, url: String?) {
        if (!url.isNullOrBlank()) onUrl(url)
        onState(PageLoadState.Success)
    }
    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        if (request?.isForMainFrame != true) return
        val desc = error?.description?.toString() ?: "Unknown error"
        onState(PageLoadState.Failed(desc, request.url?.toString()))
    }
}
