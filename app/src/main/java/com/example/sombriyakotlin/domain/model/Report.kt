package com.example.sombriyakotlin.domain.model

import android.graphics.Bitmap


data class Report(
    val id: String,
    val calification: Int,
    val description: String,
    val rentalId: String,
    val image: Bitmap
)
