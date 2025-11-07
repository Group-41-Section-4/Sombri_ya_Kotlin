package com.example.sombriyakotlin.domain.usecase.user

data class UserUseCases(
    val createUserUseCase: CreateUserUseCase,
    val getUserUseCase: GetUserUseCase,
    val refreshUserUseCase: RefreshUserUseCase,
    val logInUserUseCases: LogInUserUseCases,
    val getUserDistance: GetUserDistance,
    val googleLogInUserUseCases: GoogleLogInUserCases,
    val closeSessionUseCase: CloseSessionUseCase
)
