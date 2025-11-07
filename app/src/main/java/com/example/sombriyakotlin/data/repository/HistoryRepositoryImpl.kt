package com.example.sombriyakotlin.data.repository

import com.example.sombriyakotlin.data.datasource.History
import com.example.sombriyakotlin.data.datasource.HistoryLocalDataSource
import HistoryItem
import com.example.sombriyakotlin.domain.repository.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val local: HistoryLocalDataSource
) : HistoryRepository {

    private fun entityToDomain(e: History) =
        HistoryItem(id = e.id, date = e.date, durationMinutes = e.durationMinutes, time = e.time)

    private fun domainToEntity(d: HistoryItem) =
        History(id = d.id, date = d.date, durationMinutes = d.durationMinutes, time = d.time)

    override suspend fun save(item: HistoryItem) {

        local.insert(domainToEntity(item))
    }

    override suspend fun getAll(): List<HistoryItem> =
        local.getAll().map { entityToDomain(it) }
}
