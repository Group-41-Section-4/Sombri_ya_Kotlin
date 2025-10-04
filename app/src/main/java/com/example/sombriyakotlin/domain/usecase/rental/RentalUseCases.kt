package com.example.sombriyakotlin.domain.usecase.rental


data class RentalUseCases(
    val createRentalUseCase: CreateRentalUseCase,
    val endRentalUseCase: EndRentalUseCase,
    val getRentalsUserUseCase: GetRentalUserUseCase
    )
