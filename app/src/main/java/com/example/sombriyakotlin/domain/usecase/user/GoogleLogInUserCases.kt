package com.example.sombriyakotlin.domain.usecase.user

import com.example.sombriyakotlin.domain.model.GoogleLogIn
import com.example.sombriyakotlin.domain.repository.UserRepository
import javax.inject.Inject

class GoogleLogInUserCases @Inject constructor(private val repo: UserRepository) {
    suspend operator fun invoke(googleLogIn: GoogleLogIn) = repo.googleLogIn(googleLogIn)
}