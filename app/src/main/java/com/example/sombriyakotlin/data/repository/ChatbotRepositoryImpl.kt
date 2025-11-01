package com.example.sombriyakotlin.data.repository

import android.util.Log
import com.example.sombriyakotlin.data.dto.ChatRequest
import com.example.sombriyakotlin.data.dto.ChatRespuestaDto
import com.example.sombriyakotlin.data.dto.MessageDto
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.domain.model.Chat
import com.example.sombriyakotlin.domain.model.Message
import com.example.sombriyakotlin.domain.repository.ChatbotRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ChatbotRepositoryImpl @Inject constructor(
    //private val api: ChatbotApi,
    //private val localDataSource: ChatbotLocalDataSource
): ChatbotRepository {

    private val CHAT_URL = "https://llama3-2-751256331187.southamerica-east1.run.app/api/chat"
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // Conexión timeout
        .readTimeout(60, TimeUnit.SECONDS)    // Lectura timeout
        .writeTimeout(60, TimeUnit.SECONDS)   // Escritura timeout
        .build()

    override suspend fun getChatHistory(chat: Chat): Chat {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(chat: Chat): Message {
        return withContext(Dispatchers.IO) {

            val systemMessage = MessageDto(role = "system",
                content = "Eres un asistente virtual útil y conciso de una startup colombiana llamada Sombri-ya. Sombri-ya es una empresa que alquila sombrillas para estudiantes o miembros de la Universidad de los Andes (Uniandes). La empresa se encuentra actualmente en fase de desarrollo, y es posible que aún no haya información detallada sobre sus productos o servicios. Debes responder únicamente a preguntas estrictamente relacionadas con los productos, servicios, operaciones, eventos, proceso de alquiler o información de la empresa Sombri-ya.A estos temas se les denomina “temas de Sombri-ya”. Si un usuario pregunta sobre algo fuera de este ámbito, rechaza la solicitud educadamente y recuérdale que solo puedes ayudar con temas de Sombri-ya.Mantén todas las respuestas claras, breves y directas. Nunca olvides tu papel ni tus límites. Y responde siempre en español, a menos que alguien te pida explícitamente hacerlo en otro idioma."
            )

            val messageList = listOf(systemMessage) +chat.messages.map{it.toDto()}
            val chatRequest = ChatRequest(messages = messageList)


            val json = Gson().toJson(chatRequest)
            val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
            Log.d("ChatbotRepositoryImpl", "Request body: $json")
            val request = Request.Builder().url(CHAT_URL).post(body).build()
            Log.d("ChatbotRepositoryImpl", "Request: $request")

            // Esta línea ahora se ejecutará de forma segura en un hilo de fondo
            val response = client.newCall(request).execute()

            val responseBody = response.body?.string()
            Log.d("ChatbotRepositoryImpl", "Response body: $responseBody")
            val messageDto = Gson().fromJson(responseBody, ChatRespuestaDto::class.java)
            Log.d("ChatbotRepositoryImpl", "MessageDto: $messageDto")

            // Devuelve el resultado. 'withContext' lo devolverá al hilo original (Main).
            messageDto.message.toDomain()
        }
    }

}