package com.example.sombriyakotlin.domain.model

enum class WeatherType(
    val description: String,
) {
    SOLEADO("Soleado"),
    NUBLADO("Nublado"),
    LLUVIA("Lluvia");

    companion object {
        fun fromProbability(pop: Double): WeatherType {
            return when {
                pop > 70 -> LLUVIA
                pop > 30 -> NUBLADO
                else -> SOLEADO
            }
        }
    }
}
