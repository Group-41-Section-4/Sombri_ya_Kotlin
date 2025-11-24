package com.example.sombriyakotlin.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DataStoreSettingsStorage(
    private val dataStore: DataStore<Preferences>
)  {

    private val KEY_LOCATION_CONSENT = booleanPreferencesKey("location_consent_given") // true/false
    private val KEY_LOCATION_CONSENT_ASKED = booleanPreferencesKey("location_consent_asked") // si ya preguntó

    fun isLocationConsentGiven(): Flow<Boolean?> =
        dataStore.data
            .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map { prefs ->
                if (!prefs.contains(KEY_LOCATION_CONSENT_ASKED)) null
                else prefs[KEY_LOCATION_CONSENT] ?: false
            }

    suspend fun setLocationConsent(given: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_LOCATION_CONSENT] = given
            prefs[KEY_LOCATION_CONSENT_ASKED] = true
        }
    }

}
