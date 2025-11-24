package com.example.sombriyakotlin.domain.usecase.location

import com.example.sombriyakotlin.domain.repository.LocationRepository
import javax.inject.Inject

class IsLocationConsentGiven @Inject constructor(private val repo: LocationRepository) {
    operator fun invoke() = repo.isLocationConsentGiven()
}
