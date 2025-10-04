package com.example.sombriyakotlin.data.di

import androidx.viewbinding.BuildConfig
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

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindRentalRepository(rentalRepositoryImpl: RentalRepositoryImpl): RentalRepository
    companion object {
        @Provides
        @Singleton
        fun provideWeatherRepository():WeatherRepository{
                  return WeatherRepositoryImpl(apiKey = "64a018d01eba547f998be6d43c606c80")

    }
    }

}