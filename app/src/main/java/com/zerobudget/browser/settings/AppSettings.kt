package com.zerobudget.browser.settings

data class AppSettings(
    val homeUrl: String = DEFAULT_HOME,
    val searchUrlTemplate: String = DEFAULT_SEARCH_TEMPLATE
) {
    fun searchUrl(query: String): String {
        return searchUrlTemplate.replace("{query}", java.net.URLEncoder.encode(query, "UTF-8"))
    }

    companion object {
        const val DEFAULT_HOME = "https://www.google.com"
        const val DEFAULT_SEARCH_TEMPLATE = "https://www.google.com/search?q={query}"
    }
}
