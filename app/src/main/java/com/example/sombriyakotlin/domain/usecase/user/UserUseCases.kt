package com.example.sombriyakotlin.domain.usecase.user

data class UserUseCases(
    val createUserUseCase: CreateUserUseCase,
    val getUserUseCase: GetUserUseCase,
    val refreshUserUseCase: RefreshUserUseCase,
    val getUserDistance: GetUserDistance
)
