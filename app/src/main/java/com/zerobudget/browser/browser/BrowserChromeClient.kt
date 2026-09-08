package com.zerobudget.browser.browser

import android.webkit.WebChromeClient
import android.webkit.WebView

class BrowserChromeClient(
    private val onProgress: (Int) -> Unit,
    private val onTitle: (String?) -> Unit
) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        onProgress(newProgress)
    }
    override fun onReceivedTitle(view: WebView?, title: String?) {
        onTitle(title)
    }
}
