package com.example.sombriyakotlin.domain.repository

import com.example.sombriyakotlin.domain.model.Chat
import com.example.sombriyakotlin.domain.model.Message

interface ChatbotRepository {
    suspend fun getChatHistory(): Chat
    suspend fun sendMessage(chat: Chat): Message

    suspend fun getMessagesBefore(timestamp: Long, limit: Int): List<Message>



}
