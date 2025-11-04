package com.example.sombriyakotlin.data.di

import com.example.sombriyakotlin.BuildConfig
import com.example.sombriyakotlin.data.api.RentalApi
import com.example.sombriyakotlin.data.api.UserApi
import com.example.sombriyakotlin.data.datasource.RentalLocalDataSource
import com.example.sombriyakotlin.data.datasource.UserLocalDataSource
import com.example.sombriyakotlin.data.network.NetworkRepositoryImpl
import com.example.sombriyakotlin.data.repository.ChatbotRepositoryImpl
import com.example.sombriyakotlin.data.repository.RentalRepositoryImpl
import com.example.sombriyakotlin.data.repository.StationRepositoryImpl
import com.example.sombriyakotlin.data.repository.UserRepositoryImpl
import com.example.sombriyakotlin.domain.repository.ChatbotRepository
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
import javax.inject.Named
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
        fun provideChatRepository(): ChatbotRepository{
            return ChatbotRepositoryImpl()
        }
    }

    @Binds
    @Singleton
    abstract fun bindStationRepository(stationRepositoryImpl: StationRepositoryImpl): StationRepository

    @Binds
    @Singleton
    abstract fun bindNetworkRepository(
        impl: NetworkRepositoryImpl
    ): NetworkRepository
}