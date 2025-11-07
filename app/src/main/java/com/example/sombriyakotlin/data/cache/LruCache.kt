package com.example.sombriyakotlin.data.cache

import javax.inject.Singleton

@Singleton
class LruCache<K, V>(private val maxSize: Int) {

    private val cache: LinkedHashMap<K, V> = object : LinkedHashMap<K, V>(0, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
            return size > maxSize
        }
    }

    fun get(key: K): V? = cache[key]

    fun put(key: K, value: V) {
        cache[key] = value
    }

    fun values(): List<V> = cache.values.toList()
}
