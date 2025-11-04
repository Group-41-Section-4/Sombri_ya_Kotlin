package com.example.sombriyakotlin.domain.di

import com.example.sombriyakotlin.domain.repository.ChatbotRepository
import com.example.sombriyakotlin.domain.repository.NetworkRepository
import com.example.sombriyakotlin.domain.repository.StationRepository
import com.example.sombriyakotlin.domain.repository.RentalRepository
import com.example.sombriyakotlin.domain.repository.UserRepository
import com.example.sombriyakotlin.domain.usecase.ObserveConnectivityUseCase
import com.example.sombriyakotlin.domain.usecase.chatbot.ChatbotUseCases
import com.example.sombriyakotlin.domain.usecase.chatbot.GetChatHistoryUseCase
import com.example.sombriyakotlin.domain.usecase.chatbot.SendMessageUseCase
import com.example.sombriyakotlin.domain.usecase.stations.GetStationsUseCase
import com.example.sombriyakotlin.domain.usecase.stations.StationsUseCases
import com.example.sombriyakotlin.domain.usecase.rental.CreateRentalUseCase
import com.example.sombriyakotlin.domain.usecase.rental.EndRentalUseCase
import com.example.sombriyakotlin.domain.usecase.rental.GetRentalUserUseCase
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.rental.getRentalsHystoryUserUseCase
import com.example.sombriyakotlin.domain.usecase.stations.GetStationByTagUseCase
import com.example.sombriyakotlin.domain.usecase.user.CreateUserUseCase
import com.example.sombriyakotlin.domain.usecase.user.GetUserDistance
import com.example.sombriyakotlin.domain.usecase.user.GetUserUseCase
import com.example.sombriyakotlin.domain.usecase.user.GoogleLogInUserCases
import com.example.sombriyakotlin.domain.usecase.user.LogInUserUseCases
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
            refreshUserUseCase = RefreshUserUseCase(repo),
            logInUserUseCases = LogInUserUseCases(repo),
            getUserDistance = GetUserDistance(repo),
            googleLogInUserUseCases = GoogleLogInUserCases(repo)
        )
    }

    @Provides
    @Singleton
    fun provideRentalUseCases(repo: RentalRepository): RentalUseCases {
        return RentalUseCases(
            createRentalUseCase = CreateRentalUseCase(repo),
            endRentalUseCase = EndRentalUseCase(repo),
            getRentalsUserUseCase = GetRentalUserUseCase(repo),
            getRentalsHystoryUserUseCase = getRentalsHystoryUserUseCase(repo),
            getCurrentRentalUseCase = com.example.sombriyakotlin.domain.usecase.rental.GetCurrentRentalUseCase(repo),
            setCurrentRentalUseCase = com.example.sombriyakotlin.domain.usecase.rental.SetCurrentRentalUseCase(repo)
        )
    }

    @Provides
    @Singleton
    fun provideStationRepository(repo: StationRepository): StationsUseCases {
        return StationsUseCases(
        GetStationsUseCase(repo),
            GetStationByTagUseCase(repo)
        )
    }

    @Provides
    @Singleton
    fun provideChatbotUseCases(repo: ChatbotRepository): ChatbotUseCases {
        return ChatbotUseCases(
            sendMessageUseCase = SendMessageUseCase(repo),

            getChatHistoryUseCase = GetChatHistoryUseCase(repo)

        )
    }

    @Provides
    @Singleton
    fun provideConectivityUseCases(repo: NetworkRepository): ObserveConnectivityUseCase {
        return ObserveConnectivityUseCase(repo)
    }

}
