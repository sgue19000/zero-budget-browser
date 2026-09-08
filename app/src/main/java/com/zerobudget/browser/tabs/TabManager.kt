package com.zerobudget.browser.tabs

import java.util.UUID

class TabManager(homeUrl: String) {
    private val tabs = mutableListOf<Tab>()
    var activeIndex: Int = 0
        private set

    init {
        tabs.add(Tab(id = UUID.randomUUID().toString(), url = homeUrl, title = "Home"))
    }

    fun active(): Tab = tabs[activeIndex]
    fun all(): List<Tab> = tabs.toList()

    fun updateActive(url: String, title: String) {
        val tab = active()
        tab.url = url
        tab.title = title.ifBlank { url }
    }

    fun add(url: String, title: String = url): Tab {
        val tab = Tab(id = UUID.randomUUID().toString(), url = url, title = title)
        tabs.add(tab)
        activeIndex = tabs.lastIndex
        return tab
    }
}
