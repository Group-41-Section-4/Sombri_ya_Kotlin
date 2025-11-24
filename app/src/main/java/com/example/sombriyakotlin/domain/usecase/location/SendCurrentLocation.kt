package com.example.sombriyakotlin.domain.usecase.location

import com.example.sombriyakotlin.domain.model.CreateLocation
import com.example.sombriyakotlin.domain.repository.LocationRepository
import javax.inject.Inject

class SendCurrentLocation @Inject constructor(private val repo: LocationRepository) {
    suspend operator fun invoke(location:CreateLocation) = repo.sendCurrentLocation(location)

}