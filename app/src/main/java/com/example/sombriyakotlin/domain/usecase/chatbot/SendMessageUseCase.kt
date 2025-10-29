package com.example.sombriyakotlin.domain.usecase.chatbot

import com.example.sombriyakotlin.domain.model.Chat
import com.example.sombriyakotlin.domain.model.Message
import com.example.sombriyakotlin.domain.repository.ChatbotRepository
import javax.inject.Inject

data class SendMessageUseCase @Inject constructor(
    private val chatbotRepository: ChatbotRepository
){
    suspend fun invoke(chat: Chat): Message {
        return chatbotRepository.sendMessage(chat)
    }
}

