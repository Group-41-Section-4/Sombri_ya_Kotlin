package com.example.sombriyakotlin.domain.model

enum class NotificationType { WEATHER, SUBSCRIPTION, RENTAL }

data class Notification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val time: String
)
