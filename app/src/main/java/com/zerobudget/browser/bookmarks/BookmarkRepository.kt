package com.zerobudget.browser.bookmarks

import com.zerobudget.browser.storage.JsonFileStore
import org.json.JSONArray
import java.io.File

class BookmarkRepository(file: File) {
    private val store = JsonFileStore(file)

    @Synchronized
    fun add(url: String, title: String, createdAt: Long = System.currentTimeMillis()): Boolean {
        if (url.isBlank()) return false
        val items = load().toMutableList()
        if (items.any { it.url == url }) return false
        items.add(0, Bookmark(url, title.ifBlank { url }, createdAt))
        persist(items)
        return true
    }

    @Synchronized
    fun load(): List<Bookmark> {
        val array = store.readArray()
        val out = ArrayList<Bookmark>(array.length())
        for (i in 0 until array.length()) {
            val o = array.optJSONObject(i) ?: continue
            val url = o.optString("url")
            if (url.isBlank()) continue
            out.add(Bookmark(url, o.optString("title", url), o.optLong("createdAt", 0L)))
        }
        return out
    }

    @Synchronized
    fun contains(url: String): Boolean = load().any { it.url == url }

    @Synchronized
    fun remove(url: String) { persist(load().filterNot { it.url == url }) }

    private fun persist(items: List<Bookmark>) {
        val array = JSONArray()
        items.forEach { e -> array.put(JsonFileStore.obj("url" to e.url, "title" to e.title, "createdAt" to e.createdAt)) }
        store.writeArray(array)
    }
}
