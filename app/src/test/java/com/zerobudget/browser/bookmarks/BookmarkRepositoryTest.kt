package com.zerobudget.browser.bookmarks

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.io.path.createTempDirectory

class BookmarkRepositoryTest {
    @Test fun createAndRetrieve() {
        val dir = createTempDirectory("bm").toFile()
        val repo = BookmarkRepository(File(dir, "bookmarks.json"))
        assertTrue(repo.add("https://example.com", "Example"))
        assertFalse(repo.add("https://example.com", "Example"))
        assertEquals(1, repo.load().size)
        assertTrue(repo.contains("https://example.com"))
        repo.remove("https://example.com")
        assertFalse(repo.contains("https://example.com"))
    }
}
