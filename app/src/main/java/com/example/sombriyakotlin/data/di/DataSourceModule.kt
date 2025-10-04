package com.example.sombriyakotlin.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.sombriyakotlin.data.datasource.UserLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    private const val USER_PREFERENCES = "user_preferences"

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(USER_PREFERENCES)
        }
    }

    @Provides
    @Singleton
    fun provideUserLocalDataSource(
        dataStore: DataStore<Preferences>
    ): UserLocalDataSource = UserLocalDataSource(dataStore)
}
