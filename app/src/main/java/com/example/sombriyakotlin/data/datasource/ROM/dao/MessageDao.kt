package com.example.sombriyakotlin.data.datasource.ROM.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sombriyakotlin.data.datasource.ROM.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageEntity): Long

    @Update
    suspend fun update(message: MessageEntity)

    @Query("SELECT * FROM messages ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecent( limit: Int): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE status = :status")
    suspend fun getByStatus(status: String): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE localId = :localId")
    suspend fun getByLocalId(localId: Int): MessageEntity?

    @Query("SELECT * FROM messages WHERE timestamp < :before ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getMessagesBefore(before: Long, limit: Int): List<MessageEntity>


    // Flow para UI reactiva
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun observeConversation(): Flow<List<MessageEntity>>
}
