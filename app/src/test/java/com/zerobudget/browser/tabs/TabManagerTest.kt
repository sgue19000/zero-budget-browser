package com.zerobudget.browser.tabs

import org.junit.Assert.assertEquals
import org.junit.Test

class TabManagerTest {
    @Test fun startsWithOneTabAndCanAdd() {
        val manager = TabManager("https://www.google.com")
        assertEquals(1, manager.all().size)
        assertEquals("https://www.google.com", manager.active().url)
        manager.updateActive("https://example.com", "Example")
        assertEquals("Example", manager.active().title)
        manager.add("https://other.test", "Other")
        assertEquals(2, manager.all().size)
        assertEquals("https://other.test", manager.active().url)
    }
}
