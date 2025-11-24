package com.example.sombriyakotlin.data.repository

import android.util.Log
import com.example.sombriyakotlin.data.datasource.DataStoreSettingsStorage
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.data.serviceAdapter.LocationApi
import com.example.sombriyakotlin.domain.model.CreateLocation
import com.example.sombriyakotlin.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationApi: LocationApi,
    private val local: DataStoreSettingsStorage
): LocationRepository {
    override suspend fun sendCurrentLocation(location: CreateLocation) {
        val resp = locationApi.createRental(location.toDto())
        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string()
            Log.e("LocationRepository", "Status ${resp.code()} -> $err")
        } else {
            Log.d("LocationRepository","OK, body: ${resp.body()?.string() ?: "empty"}")
        }
    }

    override fun isLocationConsentGiven(): Flow<Boolean?> {
        return local.isLocationConsentGiven()
    }

    override suspend fun setLocationConsent(sent: Boolean) {
        return local.setLocationConsent(sent)
    }

}