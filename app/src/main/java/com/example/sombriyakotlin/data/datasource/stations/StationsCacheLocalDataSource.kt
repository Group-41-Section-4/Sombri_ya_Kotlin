package com.example.sombriyakotlin.data.datasource.stations

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class StationsCacheLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {
    private val KEY_JSON = stringPreferencesKey("stations_json")
    private val KEY_TS   = longPreferencesKey("stations_ts")

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun saveStations(list: List<SimpleStationCache>) {
        val payload = json.encodeToString(list)                 // ✅ sin serializer()
        dataStore.edit { p ->
            p[KEY_JSON] = payload
            p[KEY_TS]   = System.currentTimeMillis()
        }
    }

    suspend fun getStationsOrEmpty(): List<SimpleStationCache> {
        val pref = dataStore.data.first()
        val raw = pref[KEY_JSON] ?: return emptyList()
        return runCatching { json.decodeFromString<List<SimpleStationCache>>(raw) }
            .getOrElse { emptyList() }
    }

    suspend fun isFresh(ttlMillis: Long): Boolean {
        val pref = dataStore.data.first()
        val ts = pref[KEY_TS] ?: return false
        return System.currentTimeMillis() - ts <= ttlMillis
    }

    suspend fun clear() {
        dataStore.edit { p ->
            p.remove(KEY_JSON)
            p.remove(KEY_TS)
        }
    }
}
