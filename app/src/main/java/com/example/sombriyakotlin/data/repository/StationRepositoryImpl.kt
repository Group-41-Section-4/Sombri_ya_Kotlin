package com.example.sombriyakotlin.data.repository

import com.example.sombriyakotlin.data.api.StationApi
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.domain.model.Localization
import com.example.sombriyakotlin.domain.model.Station
import com.example.sombriyakotlin.domain.repository.StationRepository
import javax.inject.Inject

class StationRepositoryImpl @Inject constructor(
   private val stationApi: StationApi
) : StationRepository {

    override suspend fun getStations(localization: Localization): List<Station> {
        val response = stationApi.getStations(localization.toDto())
        return response.map { it.toDomain() }
    }
}

// private val rentalApi: RentalApi
//): RentalRepository{
//    override suspend fun createRental(rental: Rental): Rental {
//        val response = rentalApi.createRental(rental.toDto())
//        return  response.toDomain()
//
//}
//}