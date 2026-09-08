package com.zerobudget.browser.browser

sealed class PageLoadState {
    data object Idle : PageLoadState()
    data class Loading(val progress: Int) : PageLoadState()
    data object Success : PageLoadState()
    data class Failed(val description: String, val failingUrl: String?) : PageLoadState()
}
