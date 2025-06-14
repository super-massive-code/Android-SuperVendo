package com.supermassivecode.supervendo.data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import java.time.LocalDateTime

@Entity
data class Earnings (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val total: Long
)

@Dao
interface EarningsDao {
    @Insert
    suspend fun insert(earnings: Earnings)

    @Query("SELECT * FROM Earnings ORDER BY timestamp DESC")
    fun getAll(): List<Earnings>
}
