package com.example.sombriyakotlin.data.dto

import com.google.gson.annotations.SerializedName

data class UpdateNameDto(
    @SerializedName("nombre") val nombre: String
)
