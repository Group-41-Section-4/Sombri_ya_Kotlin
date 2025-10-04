package com.example.sombriyakotlin.domain.di

import com.example.sombriyakotlin.domain.repository.RentalRepository
import com.example.sombriyakotlin.domain.repository.UserRepository
import com.example.sombriyakotlin.domain.usecase.rental.CreateRentalUseCase
import com.example.sombriyakotlin.domain.usecase.rental.EndRentalUseCase
import com.example.sombriyakotlin.domain.usecase.rental.GetRentalUserUseCase
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.user.CreateUserUseCase
import com.example.sombriyakotlin.domain.usecase.user.GetUserUseCase
import com.example.sombriyakotlin.domain.usecase.user.RefreshUserUseCase
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideUserUseCases(repo: UserRepository): UserUseCases {
        return UserUseCases(
            createUserUseCase = CreateUserUseCase(repo),
            getUserUseCase = GetUserUseCase(repo),
            refreshUserUseCase = RefreshUserUseCase(repo)
        )
    }

    @Provides
    @Singleton
    fun provideRentalUseCases(repo: RentalRepository): RentalUseCases {
        return RentalUseCases(
            createRentalUseCase = CreateRentalUseCase(repo),
            endRentalUseCase = EndRentalUseCase(repo),
            getRentalsUserUseCase = GetRentalUserUseCase(repo)
        )
    }
}
