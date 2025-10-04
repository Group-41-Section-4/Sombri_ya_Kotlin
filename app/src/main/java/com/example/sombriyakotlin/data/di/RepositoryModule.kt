package com.example.sombriyakotlin.data.di

import com.example.sombriyakotlin.data.api.RentalApi
import com.example.sombriyakotlin.data.api.UserApi
import com.example.sombriyakotlin.data.datasource.UserLocalDataSource
import com.example.sombriyakotlin.data.repository.RentalRepositoryImpl
import com.example.sombriyakotlin.data.repository.UserRepositoryImpl
import com.example.sombriyakotlin.domain.repository.RentalRepository
import com.example.sombriyakotlin.domain.repository.UserRepository
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
    }

    @Binds
    @Singleton
    abstract fun bindRentalRepository(rentalRepositoryImpl: RentalRepositoryImpl): RentalRepository
}