package com.zerobudget.browser.browser

import com.zerobudget.browser.settings.AppSettings
import java.util.Locale

object UrlInputResolver {
    private val HOST_LIKE = Regex("""^([a-zA-Z0-9-]+\.)+[a-zA-Z]{2,}(:\d+)?(/.*)?$""")

    fun resolve(raw: String, settings: AppSettings = AppSettings()): String {
        val input = raw.trim()
        if (input.isEmpty()) return settings.homeUrl
        val lower = input.lowercase(Locale.US)
        if (lower.startsWith("https://") || lower.startsWith("http://")) return input
        if (lower.startsWith("about:") || lower.startsWith("file:")) return settings.homeUrl
        if (looksLikeHost(input)) return "https://$input"
        return settings.searchUrl(input)
    }

    fun looksLikeHost(input: String): Boolean {
        val candidate = input.trim()
        if (candidate.contains(' ') || candidate.contains('\t')) return false
        return HOST_LIKE.matches(candidate) || candidate.equals("localhost", ignoreCase = true)
    }

    fun isHttpUrl(value: String): Boolean {
        val lower = value.lowercase(Locale.US)
        return lower.startsWith("https://") || lower.startsWith("http://")
    }
}
