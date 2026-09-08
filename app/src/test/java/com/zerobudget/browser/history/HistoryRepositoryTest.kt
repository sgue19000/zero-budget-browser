package com.zerobudget.browser.history

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.io.path.createTempDirectory

class HistoryRepositoryTest {
    @Test fun recordsAndRetrievesLocally() {
        val dir = createTempDirectory("hist").toFile()
        val repo = HistoryRepository(File(dir, "history.json"))
        repo.record("https://example.com", "Example", 1000)
        repo.record("https://example.org", "Org", 2000)
        val items = repo.load()
        assertEquals(2, items.size)
        assertEquals("https://example.org", items[0].url)
        assertEquals("Example", items[1].title)
        assertTrue(File(dir, "history.json").exists())
    }
    @Test fun mostRecentVisitMovesToFront() {
        val dir = createTempDirectory("hist2").toFile()
        val repo = HistoryRepository(File(dir, "history.json"))
        repo.record("https://a.test", "A", 1)
        repo.record("https://b.test", "B", 2)
        repo.record("https://a.test", "A again", 3)
        val items = repo.load()
        assertEquals(2, items.size)
        assertEquals("https://a.test", items[0].url)
        assertEquals("A again", items[0].title)
    }
}
