package com.example.sombriyakotlin.domain.usecase.rental

import com.example.sombriyakotlin.data.dto.QrStationDto
import com.google.gson.Gson
import javax.inject.Inject


class ParseQrDataUseCase @Inject constructor()  {
    fun execute(jsonString: String): QrStationDto? {
        return try {
            val gson = Gson()
            gson.fromJson(jsonString, QrStationDto::class.java)
        } catch (e: Exception) {
            null // o lanzar un resultado de error
        }
    }
}