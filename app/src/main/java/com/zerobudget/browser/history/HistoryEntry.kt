package com.zerobudget.browser.history

data class HistoryEntry(
    val url: String,
    val title: String,
    val visitedAt: Long
)
