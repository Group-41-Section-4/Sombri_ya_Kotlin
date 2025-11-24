package com.example.sombriyakotlin.domain.repository

import com.example.sombriyakotlin.domain.model.CreateLocation


interface LocationRepository {

    suspend fun sendCurrentLocation(location: CreateLocation)
    // Lógica para obtener la ubicación actual

    //persistir
    fun isLocationConsentGiven(): kotlinx.coroutines.flow.Flow<Boolean?>

    suspend fun setLocationConsent(sent: Boolean)

}