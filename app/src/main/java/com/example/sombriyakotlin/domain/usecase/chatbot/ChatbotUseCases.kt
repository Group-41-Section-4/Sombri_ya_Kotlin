package com.example.sombriyakotlin.domain.usecase.chatbot

data class ChatbotUseCases(
    val sendMessageUseCase: SendMessageUseCase,
    val getChatHistoryUseCase: GetChatHistoryUseCase
)
