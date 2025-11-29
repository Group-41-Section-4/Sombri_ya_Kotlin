package com.example.sombriyakotlin.ui.chatbot

import android.util.Log
import android.util.SparseArray
import android.util.SparseIntArray
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.data.repository.ChatbotRepositoryImpl
import com.example.sombriyakotlin.domain.model.Chat
import com.example.sombriyakotlin.domain.model.Message
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.repository.ChatbotRepository
import com.example.sombriyakotlin.domain.usecase.ObserveConnectivityUseCase
import com.example.sombriyakotlin.domain.usecase.chatbot.ChatbotUseCases
import com.example.sombriyakotlin.ui.account.ProfileScreenViewModel.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.util.size

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatbotRepository: ChatbotRepository,
    observeConnectivity: ObserveConnectivityUseCase
): ViewModel()
{
    sealed class ChatState {
        object Idle : ChatState()
        object Loading : ChatState()
        data class Success(val chat: Chat) : ChatState()
        data class Error(val message: String) : ChatState()
    }
    companion object {
        private const val MAX_CACHE = 10
    }

    val isConnected: StateFlow<Boolean> = observeConnectivity()

    private val _chatbotState = MutableStateFlow<ChatState>(ChatState.Idle)
    val chatbotState: StateFlow<ChatState> = _chatbotState

    private val _chat = MutableStateFlow(Chat( mutableListOf()))
    val chat: StateFlow<Chat> = _chat

    private var oldestLoadedTimestamp: Long =   Long.MAX_VALUE

    private val messageCache = SparseArray<Message>()
    private val indexToPosition = SparseIntArray()

    init {
        viewModelScope.launch {
            // Inicializa chat desde DB y cache
            val history = chatbotRepository.getChatHistory()
            _chat.value = history
            rebuildCache(history.messages)
            Log.d("ChatbotViewModel", "Chat history: ${history.messages}")
            if (history.messages.isEmpty()) return@launch

            oldestLoadedTimestamp = history.messages.first().timestamp
            evictIfNeeded()
//            loadMoreMessages()
        }

    }

    private fun rebuildCache(list: List<Message>) {
        messageCache.clear()
        indexToPosition.clear()
        Log.d("ChatbotViewModel", "Rebuild cache: $list")
        list.forEachIndexed { pos, msg ->
            messageCache.put(msg.position, msg)
            indexToPosition.put(pos, msg.position)
        }
        evictIfNeeded()
    }

    private fun evictIfNeeded() {
        if (messageCache.size <= MAX_CACHE) return
        val toRemove = messageCache.size - MAX_CACHE
        for (i in 0 until toRemove) {
            messageCache.removeAt(0)
        }
        Log.d("ChatbotViewModel", "Evicted $toRemove messages")
    }



    fun sendMessage(text: String) {
        viewModelScope.launch {
            _chatbotState.value = ChatState.Loading

            val currentList = _chat.value.messages.toMutableList()
            val newMessage = Message(content = text, isUser = true, timestamp = System.currentTimeMillis())
            currentList.add(newMessage)
            _chat.value = Chat(currentList)

            try {
                Log.d("ChatViewModel", "Enviando mensaje: $text")
                val reply = chatbotRepository.sendMessage(Chat(currentList))
                Log.d("ChatViewModel", "Mensaje enviado: $reply")
                updateCacheAndList(reply)
                _chatbotState.value = ChatState.Success( _chat.value )

            } catch (e: Exception) {
                // Error, deja mensaje como PENDING
                Log.e("ChatViewModel", "Error enviando mensaje: ${e.message}")
            }
        }
    }
    private fun updateCacheAndList(newMessage: Message) {
        Log.d("ChatViewModel", "updateCacheAndList: $newMessage")
        // Actualiza cache
        val key = newMessage.position ?: run {
            // si no tiene localId, no lo podemos indexar con SparseArray de forma estable.
            // guardamos con un key temporal negativo (mejor evitar, pero aquí fallback)
            val tempKey = - (messageCache.size() + 1)
            tempKey
        }
        messageCache.put(key, newMessage)

        // Actualiza lista UI
        val currentList = _chat.value.messages.toMutableList()
        val pos = indexToPosition.get(key, -1)
        if (pos >= 0) {
            currentList[pos] = newMessage
        } else {
            currentList.add(newMessage)
            indexToPosition.put(key, currentList.lastIndex)
        }
        _chat.value = Chat(currentList)

        // Evict si hace falta
        evictIfNeeded()
    }

    fun getMessageByLocalId(localId: Int): Message? {
        return messageCache[localId]
    }

    suspend fun loadMoreMessages() {
        // Trae PAGE_SIZE mensajes anteriores a oldestLoadedTimestamp
        val before = oldestLoadedTimestamp.takeIf { it != Long.MAX_VALUE } ?: System.currentTimeMillis()
        val more = chatbotRepository.getMessagesBefore(before, MAX_CACHE) // implementa en repo
        Log.d("ChatbotViewModel", "More messages: $oldestLoadedTimestamp $more")
        if (more.isNotEmpty()) {
            // asumiendo que 'more' viene ordenado de más antiguo a más reciente
            val loadedMessages = (more + _chat.value.messages).toMutableList()
            oldestLoadedTimestamp = more.first().timestamp
            more.forEach { msg ->
                val key = msg.position ?: return@forEach
                messageCache.put(key, msg)
            }
            _chat.value = Chat(loadedMessages)
            // rebuild positions
            rebuildIndexPositions()
        }


    }

    private fun rebuildIndexPositions() {
        indexToPosition.clear()
        _chat.value.messages.forEachIndexed { pos, msg ->
            val key = msg.position ?: pos
            indexToPosition.put(key, pos)
        }
    }

}