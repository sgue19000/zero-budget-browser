package com.zerobudget.browser.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.zerobudget.browser.R
import com.zerobudget.browser.bookmarks.BookmarkRepository
import com.zerobudget.browser.browser.BrowserChromeClient
import com.zerobudget.browser.browser.BrowserWebViewClient
import com.zerobudget.browser.browser.PageLoadState
import com.zerobudget.browser.browser.UrlInputResolver
import com.zerobudget.browser.databinding.ActivityBrowserBinding
import com.zerobudget.browser.history.HistoryRepository
import com.zerobudget.browser.navigation.NavigationController
import com.zerobudget.browser.settings.AppSettings
import com.zerobudget.browser.tabs.TabManager
import java.io.File

class BrowserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBrowserBinding
    private val settings = AppSettings()
    private lateinit var tabs: TabManager
    private lateinit var history: HistoryRepository
    private lateinit var bookmarks: BookmarkRepository
    private var lastFailedUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tabs = TabManager(settings.homeUrl)
        history = HistoryRepository(File(filesDir, "history.json"))
        bookmarks = BookmarkRepository(File(filesDir, "bookmarks.json"))
        configureWebView(binding.webView)
        bindControls()
        loadAddress(settings.homeUrl, updateInput = true)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (NavigationController.shouldGoBackInWeb(binding.webView.canGoBack())) {
                    binding.webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun configureWebView(webView: WebView) {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = false
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            builtInZoomControls = true
            displayZoomControls = false
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportMultipleWindows(false)
        }
        webView.webViewClient = BrowserWebViewClient(
            onState = { state -> runOnUiThread { renderState(state) } },
            onUrl = { url -> runOnUiThread { onNavigated(url) } }
        )
        webView.webChromeClient = BrowserChromeClient(
            onProgress = { p -> runOnUiThread { binding.loadingBar.progress = p } },
            onTitle = { title ->
                runOnUiThread {
                    tabs.updateActive(binding.webView.url ?: tabs.active().url, title ?: "")
                }
            }
        )
    }

    private fun bindControls() {
        binding.goButton.setOnClickListener { submitAddressBar() }
        binding.urlInput.setOnEditorActionListener { _, actionId, event ->
            val go = actionId == EditorInfo.IME_ACTION_GO ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            if (go) submitAddressBar()
            go
        }
        binding.backButton.setOnClickListener { if (binding.webView.canGoBack()) binding.webView.goBack() }
        binding.forwardButton.setOnClickListener { if (binding.webView.canGoForward()) binding.webView.goForward() }
        binding.reloadButton.setOnClickListener { binding.webView.reload() }
        binding.homeButton.setOnClickListener { loadAddress(settings.homeUrl, updateInput = true) }
        binding.retryButton.setOnClickListener {
            val target = lastFailedUrl ?: binding.webView.url ?: settings.homeUrl
            loadAddress(target, updateInput = true)
        }
        binding.bookmarkButton.setOnClickListener { saveBookmark() }
    }

    private fun submitAddressBar() {
        val resolved = UrlInputResolver.resolve(binding.urlInput.text?.toString().orEmpty(), settings)
        loadAddress(resolved, updateInput = true)
    }

    private fun loadAddress(url: String, updateInput: Boolean) {
        binding.errorPanel.visibility = View.GONE
        lastFailedUrl = null
        if (updateInput) binding.urlInput.setText(url)
        tabs.updateActive(url, tabs.active().title)
        binding.webView.loadUrl(url)
    }

    private fun onNavigated(url: String) {
        if (binding.urlInput.hasFocus().not()) binding.urlInput.setText(url)
        tabs.updateActive(url, binding.webView.title ?: url)
        if (UrlInputResolver.isHttpUrl(url) && url != "about:blank") {
            history.record(url, binding.webView.title ?: url)
        }
        refreshNavEnabled()
    }

    private fun renderState(state: PageLoadState) {
        when (state) {
            is PageLoadState.Loading -> {
                binding.errorPanel.visibility = View.GONE
                binding.loadingBar.visibility = View.VISIBLE
                binding.loadingBar.progress = state.progress
            }
            PageLoadState.Success -> {
                binding.loadingBar.visibility = View.GONE
                binding.errorPanel.visibility = View.GONE
                refreshNavEnabled()
            }
            is PageLoadState.Failed -> {
                binding.loadingBar.visibility = View.GONE
                lastFailedUrl = state.failingUrl
                binding.errorText.text = getString(R.string.error_load) + "\n" + state.description
                binding.errorPanel.visibility = View.VISIBLE
            }
            PageLoadState.Idle -> binding.loadingBar.visibility = View.GONE
        }
    }

    private fun refreshNavEnabled() {
        binding.backButton.isEnabled = binding.webView.canGoBack()
        binding.forwardButton.isEnabled = binding.webView.canGoForward()
        binding.backButton.alpha = if (binding.webView.canGoBack()) 1f else 0.35f
        binding.forwardButton.alpha = if (binding.webView.canGoForward()) 1f else 0.35f
    }

    private fun saveBookmark() {
        val url = binding.webView.url ?: return
        val title = binding.webView.title ?: url
        val added = bookmarks.add(url, title)
        Toast.makeText(this, if (added) R.string.bookmark_added else R.string.bookmark_exists, Toast.LENGTH_SHORT).show()
    }
}
