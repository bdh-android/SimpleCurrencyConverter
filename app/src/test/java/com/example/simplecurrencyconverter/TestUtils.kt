package com.example.simplecurrencyconverter

import okio.buffer
import okio.source


object TestUtils {
    fun fileReader(fileName :String): String? {
        javaClass.classLoader?.let {
            val inputStream=it.getResourceAsStream(fileName)
            val source= inputStream.source().buffer()
            return source.readString(Charsets.UTF_8)
        }
        return null
    }
}