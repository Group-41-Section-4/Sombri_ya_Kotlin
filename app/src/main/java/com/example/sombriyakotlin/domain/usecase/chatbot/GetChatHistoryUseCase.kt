package com.example.sombriyakotlin.domain.usecase.chatbot

import com.example.sombriyakotlin.domain.model.Chat
import com.example.sombriyakotlin.domain.repository.ChatbotRepository
import javax.inject.Inject

class GetChatHistoryUseCase @Inject constructor(
    private val chatbotRepository: ChatbotRepository
){
    suspend fun invoke(): Chat {
        return chatbotRepository.getChatHistory()
    }
}
