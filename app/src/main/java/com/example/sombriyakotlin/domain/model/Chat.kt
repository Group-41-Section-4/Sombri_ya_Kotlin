package com.example.sombriyakotlin.domain.model

data class Chat(
    val messages : List<Message>
)
{
    fun addMessage(message: Message): Chat {
        return this.copy(messages = this.messages + message)
    }
}
