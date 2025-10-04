package com.example.sombriyakotlin.data.di

import androidx.viewbinding.BuildConfig
import com.example.sombriyakotlin.data.api.RentalApi
import com.example.sombriyakotlin.data.api.UserApi
import com.example.sombriyakotlin.data.datasource.UserLocalDataSource
import com.example.sombriyakotlin.data.repository.RentalRepositoryImpl
import com.example.sombriyakotlin.data.repository.UserRepositoryImpl
import com.example.sombriyakotlin.domain.repository.RentalRepository
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

        @Provides
        @Singleton
        fun provideWeatherRepository():WeatherRepository{
            return WeatherRepositoryImpl(apiKey = "64a018d01eba547f998be6d43c606c80")

        }

        @Provides
        @Singleton
        fun provideRentalRepository(
            rentalApi: com.example.sombriyakotlin.data.api.RentalApi,
            rentalLocalDataSource: com.example.sombriyakotlin.data.datasource.RentalLocalDataSource
        ): com.example.sombriyakotlin.domain.repository.RentalRepository =
            com.example.sombriyakotlin.data.repository.RentalRepositoryImpl(rentalApi, rentalLocalDataSource)

    }

}