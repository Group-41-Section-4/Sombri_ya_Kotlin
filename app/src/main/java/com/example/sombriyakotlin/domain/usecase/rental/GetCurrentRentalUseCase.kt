package com.example.sombriyakotlin.domain.usecase.rental

import com.example.sombriyakotlin.domain.repository.RentalRepository
import javax.inject.Inject

class GetCurrentRentalUseCase @Inject constructor(
    private val repo: RentalRepository

){
    operator fun invoke() = repo.currentRental()

}