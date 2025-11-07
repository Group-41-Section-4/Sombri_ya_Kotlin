package com.example.sombriyakotlin.domain.usecase.history

import HistoryItem
import com.example.sombriyakotlin.domain.repository.HistoryRepository
import javax.inject.Inject

class GetHistory @Inject constructor(private val repo: HistoryRepository) {
    suspend operator fun invoke(): List<HistoryItem> = repo.getAll()
}
