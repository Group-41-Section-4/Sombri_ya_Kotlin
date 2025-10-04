package com.example.sombriyakotlin.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.sombriyakotlin.domain.model.Rental
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RentalLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val RENT_ID = intPreferencesKey("rent_id")
        private val RENT_USER_ID = stringPreferencesKey("rent_user_id")
        private val RENT_STATION_START_ID = stringPreferencesKey("rent_station_start_id")
        private val RENT_PAYMENT_METHOD_ID = stringPreferencesKey("rent_payment_method_id")
        private val RENT_START_LAT = doublePreferencesKey("rent_start_lat")
        private val RENT_START_LON = doublePreferencesKey("rent_start_lon")
        private val RENT_AUTH_TYPE = stringPreferencesKey("rent_auth_type")
        private val RENT_STATUS = stringPreferencesKey("rent_status")
        private val RENT_STARTED_AT = stringPreferencesKey("rent_started_at")
        private val RENT_ENDED_AT = stringPreferencesKey("rent_ended_at")
    }

    fun getCurrent(): Flow<Rental?> = dataStore.data.map { p ->
        val id = p[RENT_ID] ?: return@map null
        Rental(
            id = id,
            userId = p[RENT_USER_ID] ?: "",
            stationStartId = p[RENT_STATION_START_ID] ?: "",
            paymentMethodId = p[RENT_PAYMENT_METHOD_ID],
            startLat = p[RENT_START_LAT] ?: 0.0,
            startLon = p[RENT_START_LON] ?: 0.0,
            authType = p[RENT_AUTH_TYPE] ?: "",
            status = p[RENT_STATUS] ?: "",
            startedAt = p[RENT_STARTED_AT] ?: "",
            endedAt = p[RENT_ENDED_AT]
        )
    }

    suspend fun saveCurrent(r: Rental) {
        dataStore.edit { p ->
            p[RENT_ID] = r.id
            p[RENT_USER_ID] = r.userId
            p[RENT_STATION_START_ID] = r.stationStartId
            r.paymentMethodId?.let { p[RENT_PAYMENT_METHOD_ID] = it } ?: run { p.remove(RENT_PAYMENT_METHOD_ID) }
            p[RENT_START_LAT] = r.startLat
            p[RENT_START_LON] = r.startLon
            p[RENT_AUTH_TYPE] = r.authType
            p[RENT_STATUS] = r.status
            p[RENT_STARTED_AT] = r.startedAt
            r.endedAt?.let { p[RENT_ENDED_AT] = it } ?: run { p.remove(RENT_ENDED_AT) }
        }
    }

    suspend fun clear() {
        dataStore.edit { it.remove(RENT_ID) } // no hagas clear total para no borrar otros módulos
    }
}
