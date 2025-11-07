package com.example.sombriyakotlin.domain.repository

import HistoryItem


interface HistoryRepository {
    suspend fun save(item: HistoryItem)
    suspend fun getAll(): List<HistoryItem>
}

