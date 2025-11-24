package com.example.sombriyakotlin.domain.usecase.location

import com.example.sombriyakotlin.domain.repository.LocationRepository
import javax.inject.Inject

class SetLocationConsent @Inject constructor(private val repo: LocationRepository) {
    suspend operator fun invoke(sent: Boolean) = repo.setLocationConsent(sent)
}