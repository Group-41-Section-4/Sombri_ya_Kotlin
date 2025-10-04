package com.example.sombriyakotlin.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.sombriyakotlin.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_PASSWORD = stringPreferencesKey("user_password")
        private val USER_BIOMETRIC_ENABLED = stringPreferencesKey("user_biometric_enabled")
        private val USER_CREATED_AT = stringPreferencesKey("user_created_at")
    }

    fun getUser(): Flow<User?> = dataStore.data.map { prefs ->
        val id = prefs[USER_ID] ?: return@map null
        User(
            id = id,
            name = prefs[USER_NAME] ?: "",
            email = prefs[USER_EMAIL] ?: "",
            password = prefs[USER_PASSWORD] ?: "",
            biometricEnabled = prefs[USER_BIOMETRIC_ENABLED] ?: "",
            createdAt = prefs[USER_CREATED_AT] ?: ""
        )
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { prefs ->
            prefs[USER_ID] = user.id
            prefs[USER_NAME] = user.name
            prefs[USER_EMAIL] = user.email
            prefs[USER_PASSWORD] = user.password
            prefs[USER_BIOMETRIC_ENABLED] = user.biometricEnabled
            prefs[USER_CREATED_AT] = user.createdAt
        }
    }

    suspend fun clearUser() {
        dataStore.edit { it.clear() }
    }
}
