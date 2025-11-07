package com.example.sombriyakotlin.data.datasource

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import javax.inject.Inject

import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase


@Entity(tableName = "history")
data class History(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "duration_minutes") val durationMinutes: Int,  // e.g., 1 (para "Duración: 1 minutos")
    @ColumnInfo(name = "time") val time: String,          // e.g., "07:33 PM"
)

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insert(vararg history: History)
}


@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): HistoryDao
}


class HistoryLocalDataSource @Inject constructor(
    private val dao: HistoryDao
) {
    suspend fun insert(entity: History): Unit = dao.insert(entity)
    suspend fun getAll(): List<History> = dao.getAll()
}
