package com.example.sombriyakotlin.domain.usecase.rental


data class RentalUseCases(
    val createRentalUseCase: CreateRentalUseCase,
    val endRentalUseCase: EndRentalUseCase,
    val getRentalsUserUseCase: GetRentalUserUseCase,
    val getRentalsHystoryUserUseCase: getRentalsHystoryUserUseCase,
    val getCurrentRentalUseCase: GetCurrentRentalUseCase,
    val setCurrentRentalUseCase: SetCurrentRentalUseCase,
    val getActiveRentalRemoteUseCase: GetActiveRentalRemoteUseCase

)
