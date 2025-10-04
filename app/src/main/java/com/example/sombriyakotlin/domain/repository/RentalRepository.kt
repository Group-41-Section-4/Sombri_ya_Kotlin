package com.example.sombriyakotlin.domain.repository

import com.example.sombriyakotlin.domain.model.Rent

interface RentRepository {
    suspend fun CreateRent(rent: Rent): Rent
}