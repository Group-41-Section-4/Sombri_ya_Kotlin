package com.example.sombriyakotlin.data.dto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.sombriyakotlin.domain.model.Report
import android.util.Base64

data class ReportDto(
    val rentalId:String,
    val description:String,
    val someInt:Int,
    val image: String
)

data class ResponseReportDto(
    val id:String,
    val rentalId:String,
    val description:String,
    val someInt:Int,
    val imageBase64: String
)


fun ResponseReportDto.toDomain() = Report(
    id = id,
    rentalId = rentalId,
    description = description,
    calification = someInt,
    image = base64ToBitmap(imageBase64)
)



fun Report.toDto() = ReportDto(
    rentalId = rentalId,
    description = description,
    someInt = calification,
    image = image.toString()
)

fun base64ToBitmap(base64: String): Bitmap {
    val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

