package com.example.sombriyakotlin.domain.model

enum class MessageStatus { PENDING, SENT, CONFIRMED }

data class Message(
    val content: String,
    val isUser: Boolean,
    val id: String? = null,
    val status: MessageStatus = MessageStatus.PENDING,
    val position:Int = 0,
    val timestamp: Long
)
