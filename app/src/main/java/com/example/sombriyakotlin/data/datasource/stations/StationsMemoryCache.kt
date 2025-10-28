package com.example.sombriyakotlin.data.datasource.stations

object StationsMemoryCache {
    private val memory = mutableListOf<SimpleStationCache>()

    fun putAll(list: List<SimpleStationCache>) {
        memory.clear()
        memory.addAll(list)
    }

    fun snapshot(): List<SimpleStationCache> = memory.toList()

    fun clear() = memory.clear()
}
