package com.example.sombriyakotlin.data.datasource.ROM

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.TypeConverter
import com.example.sombriyakotlin.data.datasource.ROM.dao.MessageDao
import com.example.sombriyakotlin.data.datasource.ROM.entity.MessageEntity
import com.example.sombriyakotlin.domain.model.Message
import com.example.sombriyakotlin.domain.model.MessageStatus
import java.util.Date
import javax.inject.Inject


class Converters {
    @TypeConverter
    fun fromStatus(value: String): MessageStatus = MessageStatus.valueOf(value)

    @TypeConverter
    fun toStatus(status: MessageStatus): String = status.name

    @TypeConverter
    fun fromTimestamp(value: Long) = Date(value)

    @TypeConverter
    fun toTimestamp(date: Date) = date.time
}

@Database(entities = [MessageEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}


class MessageLocalDataSource @Inject constructor(
    private val dao: MessageDao
) {
    fun messageDao(): MessageDao = dao
}