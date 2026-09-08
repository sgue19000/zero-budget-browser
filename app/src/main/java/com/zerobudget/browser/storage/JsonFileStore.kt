package com.zerobudget.browser.storage

import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class JsonFileStore(private val file: File) {
    fun readArray(): JSONArray {
        if (!file.exists()) return JSONArray()
        val text = file.readText()
        if (text.isBlank()) return JSONArray()
        return JSONArray(text)
    }
    fun writeArray(array: JSONArray) {
        file.parentFile?.mkdirs()
        file.writeText(array.toString())
    }
    companion object {
        fun obj(vararg pairs: Pair<String, Any?>): JSONObject {
            val o = JSONObject()
            pairs.forEach { (k, v) -> o.put(k, v ?: JSONObject.NULL) }
            return o
        }
    }
}
