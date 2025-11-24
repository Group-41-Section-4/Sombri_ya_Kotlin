package com.example.sombriyakotlin.data.dto

import com.example.sombriyakotlin.domain.model.Message

data class MessageDto(
    val content: String,
    val role: String = "user",

)

//data class RespuestaMessageDto(
//    val content: String,
//    val id: String ?= null,
//
//    )

data class ChatRespuestaDto(
    val model: String ?= "llama3.2",
    val created_at:String ?= "2025-10-28T07:25:48.543184821Z",
    val message: MessageDto,
    val done_reason: String ?= "stop",
    val done: Boolean?= true,
    val total_duration: String ?= null,
    val load_duration:String ?= null,
    val prompt_eval_count: String? = null,
    val prompt_eval_duration: String? = null,
    val eval_count: String? = null,
    val eval_duration: String? = null,
)


data class ChatRequest(
    val model: String ?= "Llama3.2",
    val messages: List<MessageDto>,
    val stream: Boolean ?= false
)

fun Message.toDto(): MessageDto = MessageDto (
    content = content,
    role = if (isUser) "user" else "assistant"
)

fun MessageDto.toDomain(): Message = Message (
    content = content,
    isUser = false,
    timestamp = System.currentTimeMillis()
)