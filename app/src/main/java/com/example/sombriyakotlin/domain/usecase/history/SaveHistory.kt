package com.example.sombriyakotlin.domain.usecase.history

import HistoryItem
import com.example.sombriyakotlin.domain.repository.HistoryRepository
import javax.inject.Inject

class SaveHistory @Inject constructor(private val repo: HistoryRepository) {
    suspend operator fun invoke(item: HistoryItem) = repo.save(item)
}

