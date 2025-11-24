package com.example.sombriyakotlin.data.datasource.ROM.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sombriyakotlin.data.dto.MessageDto
import com.example.sombriyakotlin.domain.model.Message
import com.example.sombriyakotlin.domain.model.MessageStatus

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val content: String,
    val isUser: Boolean,
    val remoteId: String? = null, // llega del backend
    var status: MessageStatus,
    val timestamp: Long = System.currentTimeMillis()
)


fun MessageEntity.toDomain() = Message(id=remoteId, content=content, isUser=isUser, status=status, position = localId, timestamp=timestamp)
fun Message.toEntity() = MessageEntity( content=content, isUser=isUser, remoteId=id, status=status)

fun MessageEntity.toDto() = MessageDto(role=if (isUser) "user" else "assistant", content=content)