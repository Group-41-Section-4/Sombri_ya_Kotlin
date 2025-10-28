package com.example.sombriyakotlin.ui.chatbot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.data.repository.ChatbotRepositoryImpl
import com.example.sombriyakotlin.domain.model.Chat
import com.example.sombriyakotlin.domain.model.Message
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.repository.ChatbotRepository
import com.example.sombriyakotlin.domain.usecase.chatbot.ChatbotUseCases
import com.example.sombriyakotlin.ui.account.ProfileScreenViewModel.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatbotUseCases: ChatbotUseCases,
    private val chatbotRepository: ChatbotRepository
): ViewModel()
{
    sealed class ChatState {
        object Idle : ChatState()
        object Loading : ChatState()
        data class Success(val chat: Chat) : ChatState()
        data class Error(val message: String) : ChatState()
    }

    private val _chatbotState = MutableStateFlow<ChatState>(ChatState.Idle)
    val chatbotState: StateFlow<ChatState> = _chatbotState

    private val _chat = MutableStateFlow(Chat( mutableListOf()))
    val chat: StateFlow<Chat> = _chat


    fun sendMessage(text: String){
        viewModelScope.launch {
            _chatbotState.value = ChatState.Loading

            try {
                val out = Message(text,true)
                val currentMessages = _chat.value.messages.toMutableList()
                currentMessages.add(out)
                _chat.value = Chat(currentMessages)

                val chatv = _chat.value
                val reply: Message = chatbotRepository.sendMessage(chatv)

                val updated = _chat.value.messages.toMutableList()
                updated.add(reply)
                _chat.value = Chat(updated)

                _chatbotState.value = ChatState.Success(_chat.value)
                Log.d("ChatbotViewModel", "Mensaje recibido: $reply")

            } catch (e: Exception) {
                Log.e("ChatbotViewModel", "Error enviando mensaje ${e.message}", e)
                _chatbotState.value = ChatState.Error(e.message ?: "Error enviando mensaje")

            }
        }
    }

}