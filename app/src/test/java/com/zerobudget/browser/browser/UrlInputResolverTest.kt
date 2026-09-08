package com.zerobudget.browser.browser

import com.zerobudget.browser.settings.AppSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UrlInputResolverTest {
    private val settings = AppSettings()
    @Test fun httpsUrlPassesThrough() {
        assertEquals("https://example.com/x", UrlInputResolver.resolve("https://example.com/x", settings))
    }
    @Test fun hostGetsHttpsPrefix() {
        assertEquals("https://wikipedia.org", UrlInputResolver.resolve("wikipedia.org", settings))
    }
    @Test fun searchQueryGoesToConfiguredEngine() {
        val out = UrlInputResolver.resolve("open source browser", settings)
        assertTrue(out.startsWith("https://www.google.com/search?q="))
        assertTrue(out.contains("open"))
    }
    @Test fun emptyGoesHome() {
        assertEquals(AppSettings.DEFAULT_HOME, UrlInputResolver.resolve("   ", settings))
    }
    @Test fun spacedTextIsSearchNotHost() {
        assertFalse(UrlInputResolver.looksLikeHost("hello world.com"))
    }
    @Test fun aboutIsRejectedToHome() {
        assertEquals(AppSettings.DEFAULT_HOME, UrlInputResolver.resolve("about:blank", settings))
    }
}
