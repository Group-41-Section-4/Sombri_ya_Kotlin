package com.example.sombriyakotlin.data.di

import com.example.sombriyakotlin.data.serviceAdapter.RentalApi
import com.example.sombriyakotlin.data.serviceAdapter.StationApi
import com.example.sombriyakotlin.data.serviceAdapter.UserApi
import com.example.sombriyakotlin.data.cache.LruCache
import com.example.sombriyakotlin.data.datasource.DataStoreSettingsStorage
import com.example.sombriyakotlin.data.datasource.ROM.ChatDatabase
import com.example.sombriyakotlin.data.datasource.RentalLocalDataSource
import com.example.sombriyakotlin.data.datasource.UserLocalDataSource
import com.example.sombriyakotlin.data.network.NetworkRepositoryImpl
import com.example.sombriyakotlin.data.repository.ChatbotRepositoryImpl
import com.example.sombriyakotlin.data.repository.HistoryRepositoryImpl
import com.example.sombriyakotlin.data.repository.LocationRepositoryImpl
import com.example.sombriyakotlin.data.repository.RentalRepositoryImpl
import com.example.sombriyakotlin.data.repository.StationRepositoryImpl
import com.example.sombriyakotlin.data.repository.UserRepositoryImpl
import com.example.sombriyakotlin.data.serviceAdapter.LocationApi
import com.example.sombriyakotlin.domain.model.Station
import com.example.sombriyakotlin.domain.repository.ChatbotRepository
import com.example.sombriyakotlin.domain.repository.HistoryRepository
import com.example.sombriyakotlin.domain.repository.LocationRepository
import com.example.sombriyakotlin.domain.repository.NetworkRepository
import com.example.sombriyakotlin.domain.repository.RentalRepository
import com.example.sombriyakotlin.domain.repository.StationRepository
import com.example.sombriyakotlin.domain.repository.UserRepository
import com.example.sombriyakotlin.domain.repository.WeatherRepository
import com.example.sombriyakotlin.feature.notifications.data.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    companion object {
        @Provides
        @Singleton
        fun provideUserRepository(
            userApi: UserApi,
            userLocalDataSource: UserLocalDataSource
        ): UserRepository = UserRepositoryImpl(userApi, userLocalDataSource)

        @Provides @Singleton
        fun provideRentalRepository(
            rentalApi: RentalApi,
            rentalLocalDataSource: RentalLocalDataSource
        ): RentalRepository = RentalRepositoryImpl(rentalApi, rentalLocalDataSource)

        @Provides
        @Singleton
        fun provideWeatherRepository(): WeatherRepository {
            return WeatherRepositoryImpl(apiKey = com.example.sombriyakotlin.BuildConfig.OWM_API_KEY)
        }

        @Provides
        @Singleton
        fun provideLruCache(): LruCache<String, List<Station>> {
            // 100
            return LruCache(maxSize = 4)
        }

        @Provides
        @Singleton
        fun provideLocationRepository(
            locationApi : LocationApi,
            local: DataStoreSettingsStorage
        ): LocationRepository = LocationRepositoryImpl(locationApi = locationApi, local = local)

        @Provides
        @Singleton
        fun provideStationRepositoryImpl(
            cache: LruCache<String, List<Station>>,
            stationApi: StationApi,
        ): StationRepository = StationRepositoryImpl(cache, stationApi)
    }

    @Binds
    @Singleton
    abstract fun bindNetworkRepository(
        impl: NetworkRepositoryImpl
    ): NetworkRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatbotRepositoryImpl: ChatbotRepositoryImpl
    ): ChatbotRepository


}