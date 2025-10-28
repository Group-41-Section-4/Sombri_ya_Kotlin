package com.example.sombriyakotlin.domain.repository

import com.example.sombriyakotlin.domain.model.Chat
import com.example.sombriyakotlin.domain.model.Message

interface ChatbotRepository {
    suspend fun getChatHistory(chat: Chat): Chat
    suspend fun sendMessage(message: Message): Message

}
