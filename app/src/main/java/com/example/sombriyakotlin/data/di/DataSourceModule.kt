package com.example.sombriyakotlin.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.sombriyakotlin.data.datasource.AppDatabase
import com.example.sombriyakotlin.data.datasource.HistoryDao
import com.example.sombriyakotlin.data.datasource.UserLocalDataSource
import com.example.sombriyakotlin.data.repository.HistoryRepositoryImpl
import com.example.sombriyakotlin.domain.repository.HistoryRepository
import dagger.Binds
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

    @Provides
    @Singleton
    fun provideRentalLocalDataSource(
        dataStore: DataStore<Preferences>
    ): com.example.sombriyakotlin.data.datasource.RentalLocalDataSource =
        com.example.sombriyakotlin.data.datasource.RentalLocalDataSource(dataStore)


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sombriya_database" // Nombre del archivo de la base de datos
        ).build()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(database: AppDatabase): HistoryDao {
        return database.historyDao()
    }
}

