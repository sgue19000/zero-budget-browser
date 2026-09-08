package com.zerobudget.browser.history

import com.zerobudget.browser.storage.JsonFileStore
import org.json.JSONArray
import java.io.File

class HistoryRepository(file: File, private val maxEntries: Int = 200) {
    private val store = JsonFileStore(file)

    @Synchronized
    fun record(url: String, title: String, visitedAt: Long = System.currentTimeMillis()) {
        if (url.isBlank()) return
        val items = load().toMutableList()
        items.removeAll { it.url == url }
        items.add(0, HistoryEntry(url, title.ifBlank { url }, visitedAt))
        persist(items.take(maxEntries))
    }

    @Synchronized
    fun load(): List<HistoryEntry> {
        val array = store.readArray()
        val out = ArrayList<HistoryEntry>(array.length())
        for (i in 0 until array.length()) {
            val o = array.optJSONObject(i) ?: continue
            val url = o.optString("url")
            if (url.isBlank()) continue
            out.add(HistoryEntry(url, o.optString("title", url), o.optLong("visitedAt", 0L)))
        }
        return out
    }

    @Synchronized
    fun clear() { persist(emptyList()) }

    private fun persist(items: List<HistoryEntry>) {
        val array = JSONArray()
        items.forEach { e -> array.put(JsonFileStore.obj("url" to e.url, "title" to e.title, "visitedAt" to e.visitedAt)) }
        store.writeArray(array)
    }
}
