package com.example.sombriyakotlin.data.dto

import com.google.gson.annotations.SerializedName

data class UpdateEmailDto(
    @SerializedName("email") val email: String
)
